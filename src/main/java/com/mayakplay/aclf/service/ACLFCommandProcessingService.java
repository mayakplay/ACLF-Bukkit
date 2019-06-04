package com.mayakplay.aclf.service;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.mayakplay.aclf.annotation.Translated;
import com.mayakplay.aclf.event.ChannelCommandReceiveEvent;
import com.mayakplay.aclf.exception.ACLFCommandException;
import com.mayakplay.aclf.pojo.*;
import com.mayakplay.aclf.processor.CommandControllerRegistererBeanPostProcessor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.mayakplay.aclf.service.ACLFCommandProcessingService.SenderType.*;

/**
 * Created by Mayakplay on 11.05.2019.
 */
@Service
public class ACLFCommandProcessingService implements Listener {

    protected enum SenderType {CONSOLE, PLAYER_CHAT, PLAYER_CHANNEL}

    @Translated
    private static final String SOMETHING_STUPID = "";

    //region Processing
    private boolean startCommandHandling(CommandSender sender, String commandMessage, SenderType type) {
        Map<String, Map<String, CommandDefinition>> commandDefinitionsContainer = CommandControllerRegistererBeanPostProcessor.getCommandDefinitionsContainer();

        String[] s = commandMessage.split(" ");

        if (s.length == 0) return false;

        String commandNameFromMessage = s[0].toLowerCase();
        String subCommandNameFromMessage = s.length > 1 ? s[1].toLowerCase() : "";

        if (!commandDefinitionsContainer.containsKey(commandNameFromMessage)) return false;

        if (!commandDefinitionsContainer.get(commandNameFromMessage).containsKey(subCommandNameFromMessage)) {
            if (commandDefinitionsContainer.get(commandNameFromMessage).containsKey("")) subCommandNameFromMessage = "";
            else return false;
        }

        CommandDefinition commandDefinition = commandDefinitionsContainer.get(commandNameFromMessage).get(subCommandNameFromMessage);

        int commandNameLength = commandNameFromMessage.length() + subCommandNameFromMessage.length();
        if (!subCommandNameFromMessage.isEmpty()) commandNameLength++;

        String argumentsMessage = commandMessage.substring(commandNameLength).trim();

        //region Exception handling
        try {
            processCommand(sender, commandDefinition, argumentsMessage, type);
        } catch (ACLFCommandException e) {
            if (!e.getMessage().isEmpty()) {
                sender.sendMessage(ChatColor.RED + e.getMessage());
            }
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getClass().equals(ACLFCommandException.class) && e.getCause().getMessage().isEmpty()) {
                sender.sendMessage(ChatColor.RED + e.getCause().getMessage());
            }
            e.printStackTrace();
        }
        //endregion

        return true;
    }

    private void processCommand(CommandSender sender, CommandDefinition definition, String argumentsMessage, SenderType senderType) {
        checkAccess(definition, sender, senderType);

        List<Object> argumentsObjects = processArguments(definition, sender, argumentsMessage);

        invokeMethod(definition, sender, argumentsObjects);

    }

    /**
     * Checking the {@link CommandDefinition} flags at the sender of the command
     *
     * @throws ACLFCommandException if check failed
     */
    private void checkAccess(CommandDefinition definition, CommandSender sender, SenderType senderType) throws ACLFCommandException {

        //region Perms checking
        //                                                         (ops can use commands w/o perms) ↓↓↓
        if (!sender.getEffectivePermissions().containsAll(definition.getPermissionSet()) && !sender.isOp()) {
            throw new ACLFCommandException(definition.getDescriptionDefinition().getNoPermissionsMessage());
        }
        //endregion

        //region Ops only checking
        if (definition.hasOpsOnlyFlag() && !sender.isOp()) {
            throw new ACLFCommandException(definition.getDescriptionDefinition().getOpsOnlyMessage());
        }
        //endregion

        //region Players only checking
        //                                    ↓ - the difference is here
        if (definition.hasPlayerOnlyFlag() && senderType.equals(CONSOLE)) {
            throw new ACLFCommandException(definition.getDescriptionDefinition().getPlayersOnlyMessage());
        }
        //endregion

        //region Console only checking
        //                                     ↓ - the difference is here
        if (definition.hasConsoleOnlyFlag() && !senderType.equals(CONSOLE)) {
            throw new ACLFCommandException(definition.getDescriptionDefinition().getConsoleOnlyMessage());
        }
        //endregion

        //region Chat only checking
        if (definition.hasChatOnlyFlag() && senderType.equals(PLAYER_CHANNEL)) {
            throw new ACLFCommandException(definition.getDescriptionDefinition().getChatOnlyMessage());
        }
        //endregion

        //region Channel only checking
        if (definition.hasChannelOnlyFlag() && senderType.equals(PLAYER_CHAT)) {
            throw new ACLFCommandException(definition.getDescriptionDefinition().getChannelOnlyMessage());
        }
        //endregion

    }

    @NotNull
    private List<Object> processArguments(CommandDefinition definition, CommandSender sender, String argumentsMessage) throws ACLFCommandException {
        CommandResponse response = getCommandResponseFor(definition, sender, argumentsMessage);

        String[] split = argumentsMessage.split(" ");
        if (split.length > 0 && (split[0].trim().isEmpty())) split = new String[0];

        final ImmutableList<String> argumentStringList = ImmutableList.copyOf(Arrays.asList(split));
        final List<Object> parsedObjects = new ArrayList<>();
        final List<MistakeType> mistakeTypes = new ArrayList<>();

        final List<ArgumentDefinition> argumentDefinitionList = definition.getArgumentDefinitionList();

        int stringParsedIndex = 0;
        for (ArgumentDefinition argumentDefinition : argumentDefinitionList) {
            String argumentString = argumentStringList.size() > stringParsedIndex ? argumentStringList.get(stringParsedIndex) : null;

            Object object = null;

            if (argumentDefinition.isParsedFromString()) {
                MistakeType mistakeType;

                if (argumentString != null) {
                    //region Tail arg processing
                    if (argumentDefinition.isTail()) {
                        StringBuilder argumentStringBuilder = new StringBuilder();
                        for (int j = stringParsedIndex; j < argumentStringList.size(); j++) {
                            argumentStringBuilder.append(argumentStringList.get(j)).append(" ");
                        }
                        argumentString = argumentStringBuilder.toString();

                        stringParsedIndex = Integer.MAX_VALUE;
                    }
                    //endregion

                    object = parseFromGson(argumentString.trim(), argumentDefinition.getType());
                    mistakeType = object == null ? MistakeType.EXCEPTION : MistakeType.OK;
                } else {
                    mistakeType = MistakeType.NOT_SPECIFIED;
                }
                mistakeTypes.add(mistakeType);

                stringParsedIndex++;
            } else {
                object = response;
            }

            parsedObjects.add(object);

        }

        if (mistakeTypes.contains(MistakeType.EXCEPTION) || mistakeTypes.contains(MistakeType.NOT_SPECIFIED)) throwUsage(definition, mistakeTypes);
        return parsedObjects;
    }

    private static Gson gson = new Gson();
    @Nullable
    private static Object parseFromGson(String string, Class<?> type) {
        try {
            return gson.fromJson("\"" + string + "\"", type);
        } catch (Exception ignored) {
            return null;
        }
    }

    private void invokeMethod(CommandDefinition definition, CommandSender sender, List<?> objectList) {

        try {
            definition.getMethod().invoke(definition.getControllerBean(), objectList.toArray());
        } catch (Exception e) {
            if (e.getCause() != null && ACLFCommandException.class.isAssignableFrom(e.getCause().getClass())) {
                sender.sendMessage(ChatColor.RED + e.getCause().getMessage());
            } else {
                e.printStackTrace();
            }
        }

    }

    private static CommandResponse getCommandResponseFor(CommandDefinition definition, CommandSender sender, String argumentsMessage) {
        if (definition.hasConsoleSenderOnlyFlag())
            return new ConsoleCommandResponse((ConsoleCommandSender) sender, definition.getCommandName(), definition.getSubCommandName(), argumentsMessage);
        if (definition.hasPlayerOnlyFlag())
            return new PlayerCommandResponse((Player) sender, definition.getCommandName(), definition.getSubCommandName(), argumentsMessage);
      //else
            return new CommandResponse(sender, definition.getCommandName(), definition.getSubCommandName(), argumentsMessage);
    }
    //endregion

    //region Usage

    private void throwUsage(@NotNull CommandDefinition definition, @NotNull List<MistakeType> mistakesList) throws ACLFCommandException {
        StringBuilder argumentsStringBuilder = new StringBuilder();

        List<ArgumentDefinition> stringParsedArgumentDefinitionList = definition.getStringParsedArgumentDefinitionList();
        for (int i = 0; i < stringParsedArgumentDefinitionList.size(); i++) {
            MistakeType mistakeType = mistakesList.get(i) != null ? mistakesList.get(i) : MistakeType.NOT_SPECIFIED;
            ArgumentDefinition argumentDefinition = stringParsedArgumentDefinitionList.get(i);

            argumentsStringBuilder
                    .append(mistakeType.getChatColor())
                    .append("[")
                    .append(argumentDefinition.getName())
                    .append("] ");
        }

        String argumentsString = argumentsStringBuilder.toString().trim();

        throw new ACLFCommandException(definition.getDescriptionDefinition().getUsageMessage(definition, argumentsString));
    }
    //endregion

    //region Handling
    @EventHandler
    private void playerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        boolean isStarted = startCommandHandling(event.getPlayer(), event.getMessage().substring(1), SenderType.PLAYER_CHAT);

        if (isStarted) event.setCancelled(true);
    }

    @EventHandler
    private void serverCommandEvent(ServerCommandEvent event) {
        boolean isStarted = startCommandHandling(event.getSender(), event.getCommand(), CONSOLE);

        if (isStarted) event.setCommand("aclf");
    }

    @EventHandler
    private void channelCommandReceiveEvent(ChannelCommandReceiveEvent event) {
        startCommandHandling(event.getSender(), event.getCommand(), SenderType.PLAYER_CHANNEL);
    }
    //endregion

    protected enum MistakeType {
        OK(ChatColor.GREEN),
        NOT_SPECIFIED(ChatColor.GRAY),
        EXCEPTION(ChatColor.DARK_RED);

        private ChatColor chatColor;

        MistakeType(ChatColor chatColor) {
            this.chatColor = chatColor;
        }

        public ChatColor getChatColor() {
            return chatColor;
        }
    }

}