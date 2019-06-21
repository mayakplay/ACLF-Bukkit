package com.mayakplay.aclf.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 21.06.2019.
 */
public class AddonsContextsRefreshEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public AddonsContextsRefreshEvent(Object source) {
        super(source);
    }
}