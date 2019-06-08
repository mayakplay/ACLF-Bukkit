package com.mayakplay.aclf.infrastructure;

import com.mayakplay.aclf.annotation.CommandController;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 06.06.2019.
 */
@Component
public class CommandScopeRegistryBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.registerScope(CommandController.SCOPE_SENDER, new CommandSenderScope());
    }

}