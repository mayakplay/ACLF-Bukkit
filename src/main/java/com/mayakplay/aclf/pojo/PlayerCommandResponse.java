package com.mayakplay.aclf.pojo;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Mayakplay on 10.05.2019.
 */
public final class PlayerCommandResponse extends CommandResponse {

    public PlayerCommandResponse(@NotNull Player sender, @NotNull String commandName, @NotNull String subCommandName, @NotNull String argsMessage) {
        super(sender, commandName, subCommandName, argsMessage);
    }

    @Override
    @NotNull
    public Player getSender() {
        return (Player) super.getSender();
    }
}
