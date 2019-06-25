package com.mayakplay.aclf.definition.response;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Mayakplay on 10.05.2019.
 */
public final class PlayerCommandResponse extends CommandResponse {

    public PlayerCommandResponse(@NotNull Player sender, @NotNull String fullCommandName, @NotNull String argsMessage) {
        super(sender, fullCommandName, argsMessage);
    }

    @Override
    @NotNull
    public Player getSender() {
        return (Player) super.getSender();
    }
}
