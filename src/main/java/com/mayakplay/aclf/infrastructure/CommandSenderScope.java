package com.mayakplay.aclf.infrastructure;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.core.NamedThreadLocal;

import java.util.HashMap;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 15.06.2019.
 */
public class CommandSenderScope implements Scope {

    private final ThreadLocal<HashMap<String, Object>> threadScope;

    public CommandSenderScope() {
        this.threadScope = new NamedThreadLocal<>("CommandSenderThreadLocal");
    }

    @NotNull
    @Override
    public Object get(@NotNull String name, @NotNull ObjectFactory<?> objectFactory) {
        if (threadScope.get() == null)
            threadScope.set(new HashMap<>());

        final HashMap<String, Object> stringObjectHashMap = threadScope.get();

        return stringObjectHashMap.computeIfAbsent(name, s -> objectFactory.getObject());
    }

    @Override
    public Object remove(@NotNull String name) {
        HashMap<String, Object> commandSender = threadScope.get();
        threadScope.remove();
        return commandSender;
    }

    @Override
    public void registerDestructionCallback(@NotNull String name, @NotNull Runnable callback) {

    }

    @Override
    public Object resolveContextualObject(@NotNull String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return Thread.currentThread().getName();
    }
}