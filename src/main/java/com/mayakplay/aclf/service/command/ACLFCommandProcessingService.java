package com.mayakplay.aclf.service.command;

import com.google.common.collect.ImmutableList;
import com.mayakplay.aclf.ACLF;
import com.mayakplay.aclf.AddonDefinition;
import com.mayakplay.aclf.definition.ArgumentDefinition;
import com.mayakplay.aclf.definition.CommandDefinition;
import com.mayakplay.aclf.definition.response.CommandResponse;
import com.mayakplay.aclf.definition.response.ConsoleCommandResponse;
import com.mayakplay.aclf.definition.response.PlayerCommandResponse;
import com.mayakplay.aclf.event.ChannelCommandReceiveEvent;
import com.mayakplay.aclf.exception.ACLFCommandException;
import com.mayakplay.aclf.exception.ACLFCriticalException;
import com.mayakplay.aclf.infrastructure.IncomingPluginMessageListener;
import com.mayakplay.aclf.infrastructure.SenderScopeContext;
import com.mayakplay.aclf.infrastructure.SenderScopeRunnable;
import com.mayakplay.aclf.infrastructure.SenderScopeThread;
import com.mayakplay.aclf.type.ArgumentMistakeType;
import com.mayakplay.aclf.type.CommandProcessOutput;
import com.mayakplay.aclf.type.DefinitionFlag;
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
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

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
@Service
@AllArgsConstructor
public class ACLFCommandProcessingService implements Listener, CommandProcessingService {

    public static final String COMMAND_REGEX = " ";

    //region Construction
    private final CommandSenderScopeService senderScopeService;
    private final CommandContainerService containerService;
    private final CommandMessagingService messagingService;
    //endregion

    @Override
    public CommandProcessOutput handle(String message, CommandSender sender, SenderType senderType) {
        val definition = containerService.getDefinitionByMessage(message);


        if (definition == null) return CommandProcessOutput.COMMAND_DOES_NOT_FOUND;

        try {
            CommandProcessOutput commandProcessOutput = includedCommandProcessing(message, sender, senderType, definition);

            if (commandProcessOutput != CommandProcessOutput.INVALID_ARGUMENTS)
                messagingService.sendResponseMessage(commandProcessOutput, definition, sender);

            return commandProcessOutput;
        } catch (Throwable throwable) {
            String deepACLFException = SenderScopeThread.getDeepACLFException(throwable);

            messagingService.sendResponseMessage(CommandProcessOutput.EXCEPTION, definition, sender, message);

//            sender.sendMessage(ChatColor.RED + deepACLFException);

            return CommandProcessOutput.EXCEPTION;
        }
    }

    /**
     * existence checking -> access checking -> arguments checking -> OK
     */
    private CommandProcessOutput includedCommandProcessing(String message, CommandSender sender, SenderType senderType, CommandDefinition definition) {
        val afterAccessCheckingOutputType = checkAccess(definition, sender, senderType);

        if (!afterAccessCheckingOutputType.equals(CommandProcessOutput.OK))
            return afterAccessCheckingOutputType;


        //region arguments checking

        int commandNameLength = definition.getCommandName().length() + 1;
        String onlyArgumentsMessage = message.length() >= commandNameLength ? message.substring(commandNameLength) : "";

        val argumentsObjectsList = processArguments(definition, sender, onlyArgumentsMessage.trim());

        if (argumentsObjectsList == null || argumentsObjectsList.size() != definition.getArgumentDefinitions().size())
            return CommandProcessOutput.INVALID_ARGUMENTS;

        //endregion


        invoke(definition, sender, argumentsObjectsList);
        return CommandProcessOutput.OK;

    }

    private void invoke(CommandDefinition definition, CommandSender sender, List<Object> argumentsObjectsList) {

        SenderScopeContext scopeContext = senderScopeService.getContextFor(sender);

        if (scopeContext == null) return;

        SenderScopeRunnable runnable = () -> {
            assert ACLF.getAddonDefinitionScanner() != null;
            AddonDefinition definitionByPlugin = ACLF.getAddonDefinitionScanner().getDefinitionByPlugin(definition.getPlugin());

            if (definitionByPlugin == null) {
                throw new ACLFCriticalException("NOTE ME ABOUT THIS EXCEPTION! https://github.com/mayakplay");
            }

            Object bean = definitionByPlugin
                    .getContext()
                    .getBean(
                            definition
                                    .getControllerClass()
                    );

            definition.getCommandMethod().invoke(bean, argumentsObjectsList.toArray());

        };

        scopeContext.getSenderScopeThread().handleCallback(runnable);


    }


    //    region Arguments processing
    @Nullable
    private List<Object> processArguments(CommandDefinition definition, CommandSender sender, String argumentsMessage) throws ACLFCommandException {
        CommandResponse response = getCommandResponseFor(definition, sender, argumentsMessage);

        String[] split = argumentsMessage.split(COMMAND_REGEX);
        if (split.length > 0 && (split[0].trim().isEmpty())) split = new String[0];

        final ImmutableList<String> argumentStringList = ImmutableList.copyOf(Arrays.asList(split));
        final List<Object> parsedObjects = new ArrayList<>();
        final List<ArgumentMistakeType> mistakeTypes = new ArrayList<>();

        final List<ArgumentDefinition> argumentDefinitionList = definition.getArgumentDefinitions();

        int stringParsedIndex = 0;
        for (ArgumentDefinition argumentDefinition : argumentDefinitionList) {
            String argumentString = argumentStringList.size() > stringParsedIndex ? argumentStringList.get(stringParsedIndex) : null;

            Object object = null;

            if (argumentDefinition.isResponseArgument()) {
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

//                    object = parseFromGSON(argumentString.trim(), argumentDefinition.getType());
                    try {
                        object = argumentDefinition.getProcessor().parse(argumentString.trim(), argumentDefinition.getParameter().getParameterizedType());
                    } catch (Throwable throwable) {
                        object = null;
                    }
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

        if (mistakeTypes.contains(ArgumentMistakeType.EXCEPTION) || mistakeTypes.contains(ArgumentMistakeType.NOT_SPECIFIED)) {
            messagingService.sendResponseMessage(CommandProcessOutput.INVALID_ARGUMENTS, definition, sender, mistakeTypes);
            return null;
        }

        return parsedObjects;
    }

//    private void throwUsage(@NotNull CommandDefinition definition, @NotNull List<ArgumentMistakeType> mistakesList) throws ACLFCommandException {
//        StringBuilder argumentsStringBuilder = new StringBuilder();
//
//        final List<ArgumentDefinition> stringParsedArgumentDefinitionList = definition.getArgumentDefinitions().stream()
//                .filter(ArgumentDefinition::isResponseArgument)
//                .collect(Collectors.toList());
//
//        for (int i = 0; i < stringParsedArgumentDefinitionList.size(); i++) {
//            ArgumentMistakeType mistakeType = mistakesList.get(i) != null ? mistakesList.get(i) : ArgumentMistakeType.NOT_SPECIFIED;
//            ArgumentDefinition argumentDefinition = stringParsedArgumentDefinitionList.get(i);
//
//            argumentsStringBuilder
//                    .append(mistakeType.getChatColor())
//                    .append("[")
//                    .append(argumentDefinition.getName())
//                    .append("] ");
//        }
//
//        String argumentsString = argumentsStringBuilder.toString().trim();
//
//        throw new ACLFCommandException(definition.getCommandDescriptionScanner().getUsageMessage(definition, argumentsString));
//    }

    //endregion
    private static CommandResponse getCommandResponseFor(CommandDefinition definition, CommandSender sender, String argumentsMessage) {
        if (definition.getFlagsSet().contains(DefinitionFlag.CONSOLE_SENDER_ONLY))
            return new ConsoleCommandResponse((ConsoleCommandSender) sender, definition.getCommandName(), argumentsMessage);
        if (definition.getFlagsSet().contains(DefinitionFlag.PLAYER_SENDER_ONLY))
            return new PlayerCommandResponse((Player) sender, definition.getCommandName(), argumentsMessage);
        //else
        return new CommandResponse(sender, definition.getCommandName(), argumentsMessage);
    }


    //region Checking
    private CommandProcessOutput checkAccess(CommandDefinition definition, CommandSender sender, SenderType senderType) {

        //region Perms checking
        if (!sender.getEffectivePermissions().containsAll(definition.getPermissionSet()) && !sender.isOp()) {
            return CommandProcessOutput.NO_PERMISSIONS;
        }
        //endregion

        //region Ops only checking
        if (definition.getFlagsSet().contains(DefinitionFlag.OPS_ONLY) && !sender.isOp()) {
            return CommandProcessOutput.OPS_ONLY;
        }
        //endregion

        //region Players only checking
        //                                    ↓ - the difference is here
        if (definition.getFlagsSet().contains(DefinitionFlag.PLAYER_SENDER_ONLY) && senderType.equals(SenderType.CONSOLE)) {
            return CommandProcessOutput.PLAYERS_ONLY;
        }
        //endregion

        //region Console only checking
        //                                     ↓ - the difference is here
        if (definition.getFlagsSet().contains(DefinitionFlag.CONSOLE_SENDER_ONLY) && !senderType.equals(SenderType.CONSOLE)) {
            return CommandProcessOutput.CONSOLE_ONLY;
        }
        //endregion

        //region Chat only checking
        if (definition.isForChatOnly() && senderType.equals(SenderType.PLAYER_CHANNEL)) {
            return CommandProcessOutput.CHAT_ONLY;
        }
        //endregion

        //region Channel only checking

        if (definition.isForChannelOnly() && senderType.equals(SenderType.PLAYER_CHAT)) {
            return CommandProcessOutput.CHANNEL_ONLY;
        }
        //endregion

        return CommandProcessOutput.OK;
    }

    //region Event handling

    /**
     * Calls {@link #handle} method and cancels {@link PlayerCommandPreprocessEvent}
     * if the entered command exists
     * <p>
     * Invokes when player sends command via chat
     * {@link SenderType#PLAYER_CHAT} wrapping
     */
    @EventHandler
    private void playerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        CommandProcessOutput process = handle(event.getMessage().substring(1), event.getPlayer(), SenderType.PLAYER_CHAT);

        if (!process.equals(CommandProcessOutput.COMMAND_DOES_NOT_FOUND)) event.setCancelled(true);
    }

    /**
     * Calls {@link #handle} method and replaces the command
     * with a dummy if the entered command exists
     * <p>
     * Invokes when console command detected
     * {@link SenderType#CONSOLE} wrapping
     */
    @EventHandler
    private void serverCommandEvent(ServerCommandEvent event) {
        CommandProcessOutput process = handle(event.getCommand(), event.getSender(), SenderType.CONSOLE);

        if (!process.equals(CommandProcessOutput.COMMAND_DOES_NOT_FOUND)) event.setCommand(ACLFCommandContainerService.EMPTY_COMMAND_NAME);
    }

    /**
     * Calls {@link #handle} method if the entered command exists
     * <p>
     * Invokes when player sends command via command messaging channel
     * {@link SenderType#PLAYER_CHANNEL} wrapping
     *
     * @see IncomingPluginMessageListener
     */
    @EventHandler
    private void channelCommandReceiveEvent(ChannelCommandReceiveEvent event) {
        handle(event.getCommand(), event.getSender(), SenderType.PLAYER_CHANNEL);
    }
    //endregion

}