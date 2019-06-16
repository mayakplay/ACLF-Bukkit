package com.mayakplay.aclf.pojo;

import org.bukkit.ChatColor;

/**
 * Created by Mayakplay on 10.05.2019.
 */
@Deprecated
public final class DeprecatedCommandDescriptionDefinition {

    private boolean hasDisplayedInHelp;

    private String opsOnlyMessage;
    private String noPermissionsMessage;
    private String consoleOnlyMessage;
    private String playersOnlyMessage;
    private String usageMessage;
    private String chatOnlyMessage;
    private String channelOnlyMessage;

    private final DeprecatedCommandDefinition commandDefinition;

    public DeprecatedCommandDescriptionDefinition(DeprecatedCommandDefinition commandDefinition) {
        this.commandDefinition = commandDefinition;

        scanDescriptionInCommandDefinition();
    }

    private void scanDescriptionInCommandDefinition() {

    }

    public boolean hasDisplayedInHelp() {
        return hasDisplayedInHelp;
    }

    public String getOpsOnlyMessage() {
        return opsOnlyMessage;
    }

    public String getNoPermissionsMessage() {
        return noPermissionsMessage;
    }

    public String getConsoleOnlyMessage() {
        return consoleOnlyMessage;
    }

    public String getPlayersOnlyMessage() {
        return playersOnlyMessage;
    }

    public String getUsageMessage() {
        return usageMessage;
    }

    public String getChatOnlyMessage() {
        return chatOnlyMessage;
    }

    public String getChannelOnlyMessage() {
        return channelOnlyMessage;
    }

    public String getUsageMessage(DeprecatedCommandDefinition definition, String argumentsString) {
        return ChatColor.WHITE + "Usage: " + definition.getCommandName() + " " + definition.getSubCommandName() + " " + argumentsString;
    }
}
