package com.mayakplay.aclf.infrastructure;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.stereotype.Component;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 05.06.2019.
 */
@Component
public class CommandSenderScope implements Scope {

    @NotNull
    @Override
    public Object get(@NotNull String name, @NotNull ObjectFactory<?> objectFactory) {
        System.out.println("get(" + name + ", " + objectFactory.getObject().getClass().getSimpleName() + ")" );
        return "null";
    }

    @Override
    public Object remove(@NotNull String name) {
        System.out.println("remove(" + name + ")" );
        return null;
    }

    @Override
    public void registerDestructionCallback(@NotNull String name, @NotNull Runnable callback) {
        System.out.println("registerDestructionCallback(" + name + ")");
    }

    @Override
    public Object resolveContextualObject(@NotNull String key) {
        System.out.println("resolveContextualObject(" + key + ")");
        return null;
    }

    @Override
    public String getConversationId() {
        System.out.println("getConversationId()");
        return null;
    }
}