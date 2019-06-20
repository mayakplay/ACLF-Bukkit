package com.mayakplay.aclf.infrastructure;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.core.NamedThreadLocal;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 15.06.2019.
 */
public class CommandSenderScope implements Scope {

    private final ThreadLocal<CommandSender> threadScope;

    public CommandSenderScope() {
        this.threadScope = new NamedThreadLocal<>("CommandSenderThreadLocal");
    }

    @NotNull
    @Override
    public Object get(@NotNull String name, @NotNull ObjectFactory<?> objectFactory) {
        if (threadScope.get() == null) {

        }

        if (objectFactory.getObject() instanceof CommandSender) {
            return threadScope.get();
        }

        System.out.println(objectFactory.getObject());
        return objectFactory.getObject();
    }

    @Override
    public Object remove(@NotNull String name) {
        CommandSender commandSender = threadScope.get();
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