package com.mayakplay.aclf.service;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.mayakplay.aclf.definition.ArgumentDefinition;
import com.mayakplay.aclf.definition.CommandDefinition;
import com.mayakplay.aclf.definition.response.CommandResponse;
import com.mayakplay.aclf.definition.response.ConsoleCommandResponse;
import com.mayakplay.aclf.definition.response.PlayerCommandResponse;
import com.mayakplay.aclf.event.ChannelCommandReceiveEvent;
import com.mayakplay.aclf.exception.ACLFCommandException;
import com.mayakplay.aclf.infrastructure.IncomingPluginMessageListener;
import com.mayakplay.aclf.infrastructure.SenderScopeContext;
import com.mayakplay.aclf.service.interfaces.CommandContainerService;
import com.mayakplay.aclf.service.interfaces.CommandProcessingService;
import com.mayakplay.aclf.service.interfaces.CommandSenderScopeService;
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
import org.jetbrains.annotations.NotNull;
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
@Service
@AllArgsConstructor
public class ACLFCommandProcessingService implements Listener, CommandProcessingService {

    //region Construction
    private final Gson gson;
    private final AnnotationConfigApplicationContext context;
    private final CommandSenderScopeService senderScopeService;
    private final CommandContainerService containerService;

    //endregion

    @Override
    public CommandProcessOutput handle(String message, CommandSender sender, SenderType senderType) {
        try {
            CommandProcessOutput commandProcessOutput = includedCommandProcessing(message, sender, senderType);
            System.out.println(commandProcessOutput);
            return commandProcessOutput;
        } catch (Exception ignored) {
            System.out.println(CommandProcessOutput.EXCEPTION);
            return CommandProcessOutput.EXCEPTION;
        }
    }

    /**
     * existence checking -> access checking -> arguments checking -> OK
     */
    private CommandProcessOutput includedCommandProcessing(String message, CommandSender sender, SenderType senderType) {
        //region existence checking
        val definition = containerService.getDefinitionByMessage(message);

        if (definition == null)
            /*--------*/ return CommandProcessOutput.COMMAND_DOES_NOT_FOUND;
        //endregion

        //region access checking
        val afterAccessCheckingOutputType = checkAccess(definition, sender, senderType);

        if (!afterAccessCheckingOutputType.equals(CommandProcessOutput.OK))
            /*--------*/ return afterAccessCheckingOutputType;
        //endregion

        //region arguments checking

        val argumentsObjectsList = processArguments(definition, sender, message.substring(definition.getCommandName().length()));

        if (argumentsObjectsList.size() != definition.getArgumentDefinitions().size())
            /*--------*/ return CommandProcessOutput.INVALID_ARGUMENTS;

        //endregion


        System.out.println("---------------------------------------------------");
        System.out.println(definition);

        for (Object o : argumentsObjectsList) {
            System.out.println(o);
        }
        System.out.println("---------------------------------------------------");

//
        invoke(definition, sender, argumentsObjectsList);

        /*----*/
        return CommandProcessOutput.OK;

    }

    private void invoke(CommandDefinition definition, CommandSender sender, List<Object> argumentsObjectsList) {
        SenderScopeContext scopeContext = senderScopeService.getContextFor(sender);
        if (scopeContext == null) return;

        Runnable runnable = () -> {

            Object bean = context.getBean(definition.getControllerClass());

            try {
                definition.getCommandMethod().invoke(bean, argumentsObjectsList.toArray());
                throw new RuntimeException("ofqwiekhfbqjwkhevfjqgwevfmwhqevfmhqgwvef,jqhwvef,jqhwvef,qhwevf,hqwevf,h");
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        };

        scopeContext.getSenderScopeThread().handleCallback(runnable);
    }


    //    region Arguments processing
    @NotNull
    private List<Object> processArguments(CommandDefinition definition, CommandSender sender, String argumentsMessage) throws ACLFCommandException {
        CommandResponse response = getCommandResponseFor(definition, sender, argumentsMessage);

        String[] split = argumentsMessage.split(" ");
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
                    object = argumentDefinition.getProcessor().parse(argumentString.trim(), argumentDefinition.getParameter().getParameterizedType());
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

//        if (mistakeTypes.contains(ArgumentMistakeType.EXCEPTION) || mistakeTypes.contains(ArgumentMistakeType.NOT_SPECIFIED)) throwUsage(definition, mistakeTypes);
        return parsedObjects;
    }

    //endregion

    private static CommandResponse getCommandResponseFor(CommandDefinition definition, CommandSender sender, String argumentsMessage) {
        if (definition.getFlagsSet().contains(DefinitionFlag.CONSOLE_SENDER_ONLY))
            return new ConsoleCommandResponse((ConsoleCommandSender) sender, definition.getCommandName(), argumentsMessage);
        if (definition.getFlagsSet().contains(DefinitionFlag.PLAYER_SENDER_ONLY))
            return new PlayerCommandResponse((Player) sender, definition.getCommandName(), argumentsMessage);
        //else
        return new CommandResponse(sender, definition.getCommandName(), argumentsMessage);
    }


    //region Invocation
//    private void invoke(CommandDefinition definition, CommandSender sender, List<Object> argumentObjects) {
//        SenderScopeContext contextFor = senderScopeService.getContextFor(sender);
//
//        Runnable runnable = () -> {
//            Object bean = context.getBean(definition.getControllerClass());
//
//            try {
//                definition.getCommandMethod().invoke(bean, argumentObjects);
//            } catch (IllegalAccessException | InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        };
//
//
//        assert contextFor != null;
//        contextFor.getSenderScopeThread().handleCallback(runnable);
//    }
    //endregion

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
     * Calls {@link #handle} method and replaces the command
     * with a dummy if the entered command exists
     * <p>
     * Invokes when console command detected
     * {@link SenderType#CONSOLE} wrapping
     */
    @EventHandler
    private void serverCommandEvent(ServerCommandEvent event) {
        CommandProcessOutput process = handle(event.getCommand(), event.getSender(), SenderType.CONSOLE);

        if (process.equals(CommandProcessOutput.OK)) event.setCommand("aclf");
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