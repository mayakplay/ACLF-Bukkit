package com.mayakplay.aclf.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Mayakplay on 07.02.2019.
 */
public final class TickEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private byte tickNum;

    public TickEvent(byte tickNum) {
        super();
        this.tickNum = tickNum;
    }

    public TickEvent(boolean isAsync, byte tickNum) {
        super(isAsync);
        this.tickNum = tickNum;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public byte getTickNum() {
        return tickNum;
    }
}
