package com.mayakplay.aclf.processor;

import com.google.common.collect.ImmutableMap;
import com.mayakplay.aclf.annotation.CommandController;
import com.mayakplay.aclf.event.AddonsContextsRefreshEvent;
import com.mayakplay.aclf.event.ControllersClassesScanFinishedEvent;
import com.mayakplay.aclf.infrastructure.InfrastructurePostProcessor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 09.06.2019.
 */
@InfrastructurePostProcessor
public class CommandControllerScannerBeanFactoryPostProcessor implements BeanFactoryPostProcessor, ApplicationListener<AddonsContextsRefreshEvent> {

    private final HashMap<String, Class<?>> controllersMap = new HashMap<>();

    @Override
    @SneakyThrows
    public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String name : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition factoryBeanDefinition = beanFactory.getBeanDefinition(name);

            if (factoryBeanDefinition instanceof ScannedGenericBeanDefinition) {
                final ScannedGenericBeanDefinition beanDefinition = (ScannedGenericBeanDefinition) factoryBeanDefinition;
                final String beanClassName = beanDefinition.getMetadata().getClassName();
                final Class<?> beanClass = Class.forName(beanClassName);

                if (beanClass.isAnnotationPresent(CommandController.class)) {
                    controllersMap.put(name, beanClass);
                }
            }
        }
    }

    @NotNull
    private Map<String, Class<?>> getControllersMap() {
        return ImmutableMap.copyOf(controllersMap);
    }

    @Override
    public void onApplicationEvent(@NotNull AddonsContextsRefreshEvent event) {
        AnnotationConfigApplicationContext source = event.getSource();
        source.publishEvent(new ControllersClassesScanFinishedEvent(getControllersMap()));
    }
}