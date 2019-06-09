package com.mayakplay.aclf.processor;

import com.mayakplay.aclf.ACLF;
import com.mayakplay.aclf.infrastructure.InfrastructureComponent;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.PriorityOrdered;

@InfrastructureComponent
public final class BukkitEventListenerRegisterBeanPostProcessor implements BeanPostProcessor, PriorityOrdered {

    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, String beanName) throws BeansException {
        if (bean instanceof Listener) {
            ACLF plugin = ACLF.getPlugin(ACLF.class);
            Bukkit.getPluginManager().registerEvents((Listener) bean, plugin);
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}