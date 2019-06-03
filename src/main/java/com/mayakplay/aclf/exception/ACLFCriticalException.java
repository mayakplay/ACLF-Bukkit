package com.mayakplay.aclf.exception;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * Date: 18.04.2019<br/>
 * @author Konstantin
 */
public class ACLFCriticalException extends RuntimeException {

    public ACLFCriticalException() {
        Bukkit.shutdown();
    }

    public ACLFCriticalException(String message) {
        super(ChatColor.stripColor(message));

        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + message);

        Bukkit.shutdown();
    }

    public ACLFCriticalException(String message, Throwable cause) {
        super(ChatColor.stripColor(message), cause);

        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + message);

        Bukkit.shutdown();
    }

    public ACLFCriticalException(Throwable cause) {
        super(cause);

        Bukkit.shutdown();
    }

    public ACLFCriticalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(ChatColor.stripColor(message), cause, enableSuppression, writableStackTrace);

        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + message);

        Bukkit.shutdown();
    }

}
