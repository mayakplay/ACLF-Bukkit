package com.mayakplay.aclf.event;

import com.mayakplay.aclf.infrastructure.Nugget;
import lombok.AllArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 05.07.2019.
 */
@AllArgsConstructor
public final class NuggetReceiveEvent extends Event {

    private final Class<? extends Nugget> nuggetType;
    private final Nugget receivedNugget;

    private static final HandlerList handlers = new HandlerList();

    public NuggetReceiveEvent(boolean isAsync, Class<? extends Nugget> nuggetType, Nugget receivedNugget) {
        super(isAsync);
        this.nuggetType = nuggetType;
        this.receivedNugget = receivedNugget;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}