package com.mayakplay.aclf.processor;

import com.mayakplay.aclf.infrastructure.InfrastructureComponent;
import com.mayakplay.aclf.pojo.CommandDefinition;
import com.mayakplay.aclf.service.interfaces.CommandRegistryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 09.06.2019.
 */
@InfrastructureComponent
public class CommandRegistryBeanPostProcessor implements BeanPostProcessor, CommandRegistryService {



    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, String beanName) throws BeansException {


        return null;
    }

    @Override
    public boolean containsCommand(String command, String subCommand) {
        return false;
    }

    @Override
    public CommandDefinition getCommandDefinition(String command, String subCommand) {
        return null;
    }
}
