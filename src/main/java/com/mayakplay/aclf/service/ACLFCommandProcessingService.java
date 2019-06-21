package com.mayakplay.aclf.service;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.mayakplay.aclf.event.ChannelCommandReceiveEvent;
import com.mayakplay.aclf.exception.ACLFCommandException;
import com.mayakplay.aclf.infrastructure.IncomingPluginMessageListener;
import com.mayakplay.aclf.infrastructure.SenderScopeContext;
import com.mayakplay.aclf.pojo.*;
import com.mayakplay.aclf.service.interfaces.CommandProcessingService;
import com.mayakplay.aclf.service.interfaces.CommandRegistryService;
import com.mayakplay.aclf.service.interfaces.CommandSenderScopeService;
import com.mayakplay.aclf.type.ArgumentMistakeType;
import com.mayakplay.aclf.type.CommandProcessOutput;
import com.mayakplay.aclf.type.SenderType;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command existence checking -> Access checking -> arguments processing -> controller calling
 *
 * @author mayakplay
 * @version 0.0.1
 * @since 09.06.2019.
 */
@SuppressWarnings("Duplicates")
@Service
@AllArgsConstructor
public class ACLFCommandProcessingService implements Listener, CommandProcessingService {

    //region Construction
    private final Gson gson;
    private final CommandRegistryService registryService;
    private final AnnotationConfigApplicationContext context;
    private final CommandSenderScopeService senderScopeService;
    //endregion

    //region Command processing
    @Override
    public CommandProcessOutput process(String message, CommandSender sender, SenderType senderType) {
        try {
            CommandProcessOutput commandProcessOutput = includedCommandProcessing(message, sender, senderType);
            System.out.println(commandProcessOutput);
            return commandProcessOutput;
        } catch (Exception ignored) {
            System.out.println(CommandProcessOutput.EXCEPTION);
            return CommandProcessOutput.EXCEPTION;
        }
    }

    private CommandProcessOutput includedCommandProcessing(String message, CommandSender sender, SenderType senderType) throws InvocationTargetException, IllegalAccessException {
        val definition = getCommandDefinitionByMessage(message);

        if (definition == null)
/*--------*/return CommandProcessOutput.COMMAND_DOES_NOT_FOUND;

        val afterAccessCheckingOutputType = checkAccess(definition, sender, senderType);

        if (!afterAccessCheckingOutputType.equals(CommandProcessOutput.OK))
/*--------*/return afterAccessCheckingOutputType;

        val argumentsObjectsList = processArguments(definition, sender, message.substring(definition.getFullCommandNameLength()));

        if (argumentsObjectsList.size() != definition.getArgsCount())
/*--------*/return CommandProcessOutput.INVALID_ARGUMENTS;

        invoke(definition, sender, argumentsObjectsList);

/*----*/return CommandProcessOutput.OK;
    }
    //endregion

    //region Invocation
    private void invoke(DeprecatedCommandDefinition definition, CommandSender sender, List<Object> argumentObjects) {
        SenderScopeContext contextFor = senderScopeService.getContextFor(sender);

        Runnable runnable = () -> {
            Object bean = context.getBean(definition.getControllerClass());

            try {
                definition.getMethod().invoke(bean, argumentObjects);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        };


        assert contextFor != null;
        contextFor.getSenderScopeThread().handleCallback(runnable);
    }
    //endregion

    //region Checking
    /**
     * Checking the {@link DeprecatedCommandDefinition} flags at the sender of the command
     *
     * @throws ACLFCommandException with translated message if check failed
     */
    private CommandProcessOutput checkAccess(DeprecatedCommandDefinition definition, CommandSender sender, SenderType senderType) throws ACLFCommandException {

        //region Perms checking
        if (!sender.getEffectivePermissions().containsAll(definition.getPermissionSet()) && !sender.isOp()) {
            return CommandProcessOutput.NO_PERMISSIONS;
        }
        //endregion

        //region Ops only checking
        if (definition.hasOpsOnlyFlag() && !sender.isOp()) {
            return CommandProcessOutput.OPS_ONLY;
        }
        //endregion

        //region Players only checking
        //                                    ↓ - the difference is here
        if (definition.hasPlayerOnlyFlag() && senderType.equals(SenderType.CONSOLE)) {
            return CommandProcessOutput.PLAYERS_ONLY;
        }
        //endregion

        //region Console only checking
        //                                     ↓ - the difference is here
        if (definition.hasConsoleOnlyFlag() && !senderType.equals(SenderType.CONSOLE)) {
            return CommandProcessOutput.CONSOLE_ONLY;
        }
        //endregion

        //region Chat only checking
        if (definition.hasChatOnlyFlag() && senderType.equals(SenderType.PLAYER_CHANNEL)) {
            return CommandProcessOutput.CHAT_ONLY;
        }
        //endregion

        //region Channel only checking
        if (definition.hasChannelOnlyFlag() && senderType.equals(SenderType.PLAYER_CHAT)) {
            return CommandProcessOutput.CHANNEL_ONLY;
        }
        //endregion

        return CommandProcessOutput.OK;
    }

    @Nullable
    private DeprecatedCommandDefinition getCommandDefinitionByMessage(String message) {
        return registryService.getCommandDefinitionByMessage(message);
    }

    //endregion

    //region Arguments processing
    @NotNull
    private List<Object> processArguments(DeprecatedCommandDefinition definition, CommandSender sender, String argumentsMessage) throws ACLFCommandException {
        CommandResponse response = getCommandResponseFor(definition, sender, argumentsMessage);

        String[] split = argumentsMessage.split(" ");
        if (split.length > 0 && (split[0].trim().isEmpty())) split = new String[0];

        final ImmutableList<String> argumentStringList = ImmutableList.copyOf(Arrays.asList(split));
        final List<Object> parsedObjects = new ArrayList<>();
        final List<ArgumentMistakeType> mistakeTypes = new ArrayList<>();

        final List<DeprecatedArgumentDefinition> argumentDefinitionList = definition.getArgumentDefinitionList();

        int stringParsedIndex = 0;
        for (DeprecatedArgumentDefinition argumentDefinition : argumentDefinitionList) {
            String argumentString = argumentStringList.size() > stringParsedIndex ? argumentStringList.get(stringParsedIndex) : null;

            Object object = null;

            if (argumentDefinition.isParsedFromString()) {
                ArgumentMistakeType mistakeType;

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

                    object = parseFromGSON(argumentString.trim(), argumentDefinition.getType());
                    mistakeType = object == null ? ArgumentMistakeType.EXCEPTION : ArgumentMistakeType.OK;
                } else {
                    mistakeType = ArgumentMistakeType.NOT_SPECIFIED;
                }
                mistakeTypes.add(mistakeType);

                stringParsedIndex++;
            } else {
                object = response;
            }

            parsedObjects.add(object);

        }

        if (mistakeTypes.contains(ArgumentMistakeType.EXCEPTION) || mistakeTypes.contains(ArgumentMistakeType.NOT_SPECIFIED)) throwUsage(definition, mistakeTypes);
        return parsedObjects;
    }

    @Nullable
    private Object parseFromGSON(String string, Class<?> type) {
        try {
            return gson.fromJson("\"" + string + "\"", type);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static CommandResponse getCommandResponseFor(DeprecatedCommandDefinition definition, CommandSender sender, String argumentsMessage) {
        if (definition.hasConsoleSenderOnlyFlag())
            return new ConsoleCommandResponse((ConsoleCommandSender) sender, definition.getCommandName(), definition.getSubCommandName(), argumentsMessage);
        if (definition.hasPlayerSenderOnlyFlag())
            return new PlayerCommandResponse((Player) sender, definition.getCommandName(), definition.getSubCommandName(), argumentsMessage);
        //else
        return new CommandResponse(sender, definition.getCommandName(), definition.getSubCommandName(), argumentsMessage);
    }
    //endregion

    //region Usage
    private void throwUsage(@NotNull DeprecatedCommandDefinition definition, @NotNull List<ArgumentMistakeType> mistakesList) throws ACLFCommandException {
        StringBuilder argumentsStringBuilder = new StringBuilder();

        List<DeprecatedArgumentDefinition> stringParsedArgumentDefinitionList = definition.getStringParsedArgumentDefinitionList();
        for (int i = 0; i < stringParsedArgumentDefinitionList.size(); i++) {
            ArgumentMistakeType mistakeType = mistakesList.get(i) != null ? mistakesList.get(i) : ArgumentMistakeType.NOT_SPECIFIED;
            DeprecatedArgumentDefinition argumentDefinition = stringParsedArgumentDefinitionList.get(i);

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

    //region Event handling
    /**
     * Calls {@link #process} method and cancels {@link PlayerCommandPreprocessEvent}
     * if the entered command exists
     *
     * Invokes when player sends command via chat
     * {@link SenderType#PLAYER_CHAT} wrapping
     */
    @EventHandler
    private void playerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        CommandProcessOutput process = process(event.getMessage().substring(1), event.getPlayer(), SenderType.PLAYER_CHAT);

        if (process.equals(CommandProcessOutput.OK)) event.setCancelled(true);
//        SenderScopeContext contextFor = senderScopeService.getContextFor(event.getPlayer());
//        if (contextFor != null) {
//            contextFor.getSenderScopeThread().handleCallback(() -> {
//                TestController bean = context.getBean(TestController.class);
//            });
//        } else {
//            System.out.println("----------------- SENDER CONTEXT = NULL -----------------"); //test
//        }
    }

    /**
     * Calls {@link #process} method and replaces the command
     * with a dummy if the entered command exists
     *
     * Invokes when console command detected
     * {@link SenderType#CONSOLE} wrapping
     */
    @EventHandler
    private void serverCommandEvent(ServerCommandEvent event) {
//        SenderScopeContext contextFor = senderScopeService.getContextFor(event.getSender());
//
//        if (contextFor != null) {
//            contextFor.getSenderScopeThread().handleCallback(() -> {
//                TestController bean = context.getBean(TestController.class);
//            });
//        } else {
//            System.out.println("----------------- SENDER CONTEXT = NULL -----------------"); //test
//        }
        CommandProcessOutput process = process(event.getCommand(), event.getSender(), SenderType.CONSOLE);

        if (process.equals(CommandProcessOutput.OK)) event.setCommand("aclf");
    }

    /**
     * Calls {@link #process} method if the entered command exists
     *
     * Invokes when player sends command via command messaging channel
     * {@link SenderType#PLAYER_CHANNEL} wrapping
     *
     * @see IncomingPluginMessageListener
     */
    @EventHandler
    private void channelCommandReceiveEvent(ChannelCommandReceiveEvent event) {
        process(event.getCommand(), event.getSender(), SenderType.PLAYER_CHANNEL);
    }
    //endregion

}