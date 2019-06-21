package com.mayakplay.aclf.processor;

import com.google.common.collect.ImmutableMap;
import com.mayakplay.aclf.annotation.CommandController;
import com.mayakplay.aclf.event.AddonsContextsRefreshEvent;
import com.mayakplay.aclf.infrastructure.InfrastructurePostProcessor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 09.06.2019.
 */
@InfrastructurePostProcessor
public class CommandControllerScannerBeanFactoryPostProcessor implements BeanFactoryPostProcessor, ApplicationListener {

    private final HashMap<String, Class<?>> controllersMap = new HashMap<>();

    @Override
    @SneakyThrows
    public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String name : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);

            System.out.println("=======================================" + beanDefinition.getBeanClassName());

            Class<?> beanClass = Class.forName(
                    beanDefinition
                            .getBeanClassName()
            );
            if (beanClass.getAnnotation(CommandController.class) != null) {
                controllersMap.put(name, beanClass);
            }
        }
    }

    @NotNull
    public Map<String, Class<?>> getControllersMap() {
        return ImmutableMap.copyOf(controllersMap);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof AddonsContextsRefreshEvent) {
            System.out.println("CONTEXTS REFRESHED");
            System.out.println("CONTEXTS REFRESHED");
            System.out.println("CONTEXTS REFRESHED");
            System.out.println("CONTEXTS REFRESHED");
            System.out.println("CONTEXTS REFRESHED");
        }
    }
}