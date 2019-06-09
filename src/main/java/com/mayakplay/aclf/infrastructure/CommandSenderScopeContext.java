package com.mayakplay.aclf.infrastructure;

import org.bukkit.command.CommandSender;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 09.06.2019.
 */
class CommandSenderScopeContext {

    private CommandSender sender;

    CommandSenderScopeContext(CommandSender sender) {

        this.sender = sender;

    }

}
