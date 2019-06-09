package com.mayakplay.aclf.infrastructure;

import org.bukkit.command.CommandSender;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 09.06.2019.
 */
public class CommandSenderScopeContextThreadLocal {

    private static ThreadLocal<CommandSenderScopeContext> senderContext = new ThreadLocal<>();

    private CommandSenderScopeContextThreadLocal() {

    }

    public static CommandSenderScopeContext getSenderContext() {
        return senderContext.get();
    }

    public static void setSyncContext(CommandSenderScopeContext context) {
        senderContext.set(context);
    }

    public static void clear() {
        senderContext.remove();
    }

    public static void setSyncContext(CommandSender sender) {
        setSyncContext(new CommandSenderScopeContext(sender));
    }

}
