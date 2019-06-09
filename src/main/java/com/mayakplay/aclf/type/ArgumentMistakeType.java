package com.mayakplay.aclf.type;

import org.bukkit.ChatColor;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 09.06.2019.
 */
public enum  ArgumentMistakeType {

    OK(ChatColor.GREEN),
    NOT_SPECIFIED(ChatColor.GRAY),
    EXCEPTION(ChatColor.DARK_RED);

    private ChatColor chatColor;

    ArgumentMistakeType(ChatColor chatColor) {
        this.chatColor = chatColor;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

}
