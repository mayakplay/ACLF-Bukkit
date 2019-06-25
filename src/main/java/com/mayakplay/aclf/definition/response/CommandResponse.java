package com.mayakplay.aclf.definition.response;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Mayakplay on 10.05.2019.
 */
@Getter
public class CommandResponse {

    @NotNull private final CommandSender sender;
    @NotNull private final String fullCommandName;
    @NotNull private final String message;

    public CommandResponse(@NotNull CommandSender sender, @NotNull String fullCommandName, @NotNull String argsMessage) {
        this.sender = sender;
        this.fullCommandName = fullCommandName;
        this.message = argsMessage;
    }

}