package com.mayakplay.aclf.pojo;

import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Mayakplay on 10.05.2019.
 */
public class ConsoleCommandResponse extends CommandResponse {

    public ConsoleCommandResponse(@NotNull ConsoleCommandSender sender, @NotNull String commandName, @NotNull String subCommandName, @NotNull String argsMessage) {
        super(sender, commandName, subCommandName, argsMessage);
    }

    @Override
    @NotNull
    public ConsoleCommandSender getSender() {
        return (ConsoleCommandSender) super.getSender();
    }
}
