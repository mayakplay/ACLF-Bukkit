package com.mayakplay.aclf.definition;

import com.mayakplay.aclf.annotation.Documented;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.lang.reflect.AnnotatedElement;

/**
 * Class needed to scan values of {@link Documented} annotation.
 *
 *
 * @author mayakplay
 * @version 0.0.1
 * @since 23.06.2019.
 */
@Getter
public class CommandDescriptionDefinition {

    private AnnotatedElement controllerDefinition;
    private AnnotatedElement commandDefinition;

    private boolean hasDisplayedInHelp;

    private String opsOnlyMessage;
    private String noPermissionsMessage;
    private String consoleOnlyMessage;
    private String playersOnlyMessage;
    private String usageMessage;
    private String chatOnlyMessage;
    private String channelOnlyMessage;

    CommandDescriptionDefinition(AnnotatedElement controllerDefinition, AnnotatedElement commandDefinition) {
        this.controllerDefinition = controllerDefinition;
        this.commandDefinition = commandDefinition;

        scan();
    }

    private void scan() {
        Documented annotation = controllerDefinition.getAnnotation(Documented.class);
        Documented commandAnnotation = commandDefinition.getAnnotation(Documented.class);

        Documented documentedAnnotation = commandAnnotation != null ? commandAnnotation : annotation;

        if (documentedAnnotation != null) {
            this.hasDisplayedInHelp = documentedAnnotation.displayInHelp();

            opsOnlyMessage = documentedAnnotation.opsOnlyMessage();
            noPermissionsMessage = documentedAnnotation.noPermissionsMessage();
            consoleOnlyMessage = documentedAnnotation.consoleOnlyMessage();
            playersOnlyMessage = documentedAnnotation.playersOnlyMessage();
            usageMessage = documentedAnnotation.usage();
            chatOnlyMessage = documentedAnnotation.chatOnlyMessage();
            channelOnlyMessage = documentedAnnotation.channelOnlyMessage();
        } else {
            this.hasDisplayedInHelp = false;

            opsOnlyMessage = Documented.DEFAULT_OPS_ONLY_MESSAGE;
            noPermissionsMessage = Documented.DEFAULT_NO_PERMISSIONS_MESSAGE;
            consoleOnlyMessage = Documented.DEFAULT_CONSOLE_ONLY_MESSAGE;
            playersOnlyMessage = Documented.DEFAULT_PLAYERS_ONLY_MESSAGE;
            usageMessage = Documented.DEFAULT_USAGE_MESSAGE;
            chatOnlyMessage = Documented.DEFAULT_CHAT_ONLY_MESSAGE;
            channelOnlyMessage = Documented.DEFAULT_CHANNEL_ONLY_MESSAGE;
        }
    }

    public String getUsageMessage(CommandDefinition definition, String argumentsString) {
        return ChatColor.WHITE + "Usage: " + definition.getCommandName().replace(":", " ") + " " + argumentsString;
    }
}