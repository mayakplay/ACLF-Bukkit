package com.mayakplay.aclf.definition;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 23.06.2019.
 */

import com.mayakplay.aclf.annotation.Documented;

/**
 * Class needed to scan values of {@link Documented} annotation.
 */
public class CommandDescriptionScanner {

    private CommandDefinition definition;

    private boolean hasDisplayedInHelp;

    private String opsOnlyMessage;
    private String noPermissionsMessage;
    private String consoleOnlyMessage;
    private String playersOnlyMessage;
    private String usageMessage;
    private String chatOnlyMessage;
    private String channelOnlyMessage;

    CommandDescriptionScanner(CommandDefinition definition) {
        this.definition = definition;
    }

    void scan() {

        this.opsOnlyMessage = getMessage("", "");
    }

    private String getMessage(String annotationMessage, String defaultMessage) {
        return null;
    }

}