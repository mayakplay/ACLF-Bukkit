package com.mayakplay.aclf.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 05.07.2019.
 */
@Getter
@AllArgsConstructor
public final class NuggetReceiveEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final String tag;
    private final String nuggetMessage;
    private final Map<String, String> parameters;

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}