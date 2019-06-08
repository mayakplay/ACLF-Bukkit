package com.mayakplay.aclf.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 05.06.2019.
 */
public final class ContextsCreationCompletionEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}