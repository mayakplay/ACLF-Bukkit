package com.mayakplay.aclf.event;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class ChannelCommandReceiveEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player sender;
    private String command;

    public ChannelCommandReceiveEvent(Player sender, String command) {
        this.sender = sender;
        this.command = command;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public CommandSender getSender() {
        return sender;
    }

    public String getCommand() {
        return command;
    }
}
