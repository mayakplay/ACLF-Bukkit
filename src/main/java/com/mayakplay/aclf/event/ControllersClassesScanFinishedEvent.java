package com.mayakplay.aclf.event;

import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 22.06.2019.
 */
public final class ControllersClassesScanFinishedEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    private Map<String, Class<?>> controllersMap;

    public ControllersClassesScanFinishedEvent(Map<String, Class<?>> source) {
        super(source);
        this.controllersMap = source;
    }

    @Override
    public Map<String, Class<?>> getSource() {
        return controllersMap;
    }
}