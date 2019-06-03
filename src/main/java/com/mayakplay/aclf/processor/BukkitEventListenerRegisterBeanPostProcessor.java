package com.mayakplay.aclf.processor;

import com.mayakplay.aclf.ACLFPluginAdapter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

@Component
public final class BukkitEventListenerRegisterBeanPostProcessor implements BeanPostProcessor, PriorityOrdered {

    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, String beanName) throws BeansException {
        if (bean instanceof Listener) {
            ACLFPluginAdapter plugin = ACLFPluginAdapter.getPlugin(ACLFPluginAdapter.class);
            Bukkit.getPluginManager().registerEvents((Listener) bean, plugin);
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}