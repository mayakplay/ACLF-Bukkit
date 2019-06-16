package com.mayakplay.aclf.processor;

import com.mayakplay.aclf.annotation.CommandController;
import com.mayakplay.aclf.infrastructure.InfrastructurePostProcessor;
import com.mayakplay.aclf.pojo.DeprecatedCommandDefinition;
import com.mayakplay.aclf.service.interfaces.CommandRegistryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 09.06.2019.
 */
@InfrastructurePostProcessor
public class CommandRegistryBeanPostProcessor implements BeanPostProcessor, CommandRegistryService {

    private Map<String, Map<String, DeprecatedCommandDefinition>> commandDefinitionsContainer = new LinkedHashMap<>();

    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();

        CommandController commandMappingAnnotation = AnnotatedElementUtils.findMergedAnnotation(beanClass, CommandController.class);



        return null;
    }

    @Override
    public boolean containsCommand(String command, String subCommand) {
        return false;
    }

    @Override
    public DeprecatedCommandDefinition getCommandDefinition(String command, String subCommand) {
        return null;
    }

    @Override
    public DeprecatedCommandDefinition getCommandDefinitionByMessage(String message) {
        return null;
    }
}