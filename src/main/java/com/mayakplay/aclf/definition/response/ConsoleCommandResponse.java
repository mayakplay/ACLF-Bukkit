package com.mayakplay.aclf.definition.response;

import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Mayakplay on 10.05.2019.
 */
public class ConsoleCommandResponse extends CommandResponse {

    public ConsoleCommandResponse(@NotNull ConsoleCommandSender sender, @NotNull String fullCommandName, @NotNull String argsMessage) {
        super(sender, fullCommandName, argsMessage);
    }

    @Override
    @NotNull
    public ConsoleCommandSender getSender() {
        return (ConsoleCommandSender) super.getSender();
    }
}
