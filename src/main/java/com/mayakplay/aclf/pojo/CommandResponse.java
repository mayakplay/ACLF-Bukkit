package com.mayakplay.aclf.pojo;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Mayakplay on 10.05.2019.
 */
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

    public CommandResponse ok(String... message) {
        return this;
    }

    //region Getters
    @NotNull
    public CommandSender getSender() {
        return sender;
    }

    @NotNull
    public String getCommandName() {
        return commandName;
    }

    @NotNull
    public String getSubCommandName() {
        return subCommandName;
    }

    @NotNull
    public String getMessage() {
        return message;
    }
    //endregion

}