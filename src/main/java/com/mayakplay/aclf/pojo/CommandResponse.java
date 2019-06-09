package com.mayakplay.aclf.pojo;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Mayakplay on 10.05.2019.
 */
@Getter
public class CommandResponse {

    @NotNull private CommandSender sender;

    @NotNull private String commandName;
    @NotNull private String subCommandName;

    @NotNull private String message;

    public CommandResponse(@NotNull CommandSender sender, @NotNull String commandName, @NotNull String subCommandName, @NotNull String argsMessage) {
        this.sender = sender;
        this.commandName = commandName;
        this.subCommandName = subCommandName;
        this.message = argsMessage;
    }

}