package com.mayakplay.aclf.processor;

import com.mayakplay.aclf.annotation.CommandController;
import com.mayakplay.aclf.infrastructure.CommandSenderScope;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 19.06.2019.
 */
@Component
public class SenderScopeRegistryBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.registerScope(CommandController.COMMAND_SENDER_SCOPE, new CommandSenderScope());

        for (String name : beanFactory.getBeanDefinitionNames()) {

        }

    }

}