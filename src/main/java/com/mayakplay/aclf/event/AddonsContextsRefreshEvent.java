package com.mayakplay.aclf.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 21.06.2019.
 */
public final class AddonsContextsRefreshEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param sourceContext the object on which the event initially occurred (never {@code null})
     */
    public AddonsContextsRefreshEvent(AnnotationConfigApplicationContext sourceContext) {
        super(sourceContext);
    }

    @Override
    public AnnotationConfigApplicationContext getSource() {
        return (AnnotationConfigApplicationContext) super.getSource();
    }
}