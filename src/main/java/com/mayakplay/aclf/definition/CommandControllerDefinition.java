package com.mayakplay.aclf.definition;

import com.google.common.collect.ImmutableList;
import com.mayakplay.aclf.annotation.CommandMapping;
import com.mayakplay.aclf.annotation.OpsOnly;
import com.mayakplay.aclf.type.DefinitionFlag;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 15.06.2019.
 */
@Getter
public class CommandControllerDefinition implements AnnotatedElement {

    @NotNull
    private final String commandName;

    @NotNull
    private final Class<?> controllerClass;

    @NotNull
    private final String controllerBeanName;

    @NotNull
    private final List<CommandDefinition> commandDefinitionsList;

    @NotNull
    private final EnumSet<DefinitionFlag> flags;

    private CommandControllerDefinition(@NotNull Class<?> controllerClass, @NotNull String controllerBeanName) {
        this.controllerClass = controllerClass;
        this.controllerBeanName = controllerBeanName;
        this.commandDefinitionsList = new ArrayList<>();
        this.commandName = getAnnotation(CommandMapping.class).value();
        this.flags = EnumSet.allOf(DefinitionFlag.class);

        List<CommandDefinition> availableDefinitions = Arrays.stream(controllerClass.getDeclaredMethods())
                .map(method -> CommandDefinition.of(method, this))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        this.commandDefinitionsList.addAll(availableDefinitions);

        if (getAnnotation(OpsOnly.class) != null) flags.add(DefinitionFlag.OPS_ONLY);

    }

    public static CommandControllerDefinition of(@NotNull Class<?> controllerClass, @NotNull String controllerBeanName) {
        return new CommandControllerDefinition(controllerClass, controllerBeanName);
    }

    @NotNull
    public List<CommandDefinition> getCommandDefinitions() {
        return ImmutableList.copyOf(commandDefinitionsList);
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return AnnotatedElementUtils.findMergedAnnotation(controllerClass, annotationClass);
    }

    @Override
    public Annotation[] getAnnotations() {
        return controllerClass.getAnnotations();
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return controllerClass.getDeclaredAnnotations();
    }
}