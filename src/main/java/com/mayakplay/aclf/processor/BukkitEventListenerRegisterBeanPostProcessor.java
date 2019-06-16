package com.mayakplay.aclf.processor;

import com.mayakplay.aclf.ACLF;
import com.mayakplay.aclf.infrastructure.InfrastructurePostProcessor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Registers beans implements {@link Listener} as bukkit event.
 *
 * @see Listener
 * @see EventHandler
 */
@InfrastructurePostProcessor
public final class BukkitEventListenerRegisterBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, String beanName) throws BeansException {
        if (bean instanceof Listener) {
            ACLF plugin = ACLF.getPlugin(ACLF.class);
            Bukkit.getPluginManager().registerEvents((Listener) bean, plugin);
        }
        return bean;
    }

}