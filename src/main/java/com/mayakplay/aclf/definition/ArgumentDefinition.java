package com.mayakplay.aclf.definition;

import com.google.common.collect.ImmutableList;
import com.mayakplay.aclf.annotation.Argument;
import com.mayakplay.aclf.definition.response.CommandResponse;
import com.mayakplay.aclf.service.interfaces.CommandContainerService;
import com.mayakplay.aclf.stereotype.ArgumentProcessor;
import com.mayakplay.aclf.type.DefinitionFlag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 17.06.2019.
 */
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArgumentDefinition {

    private final String name;
    private final boolean isResponseArgument;
    private final boolean isTail;

    private final ArgumentProcessor processor;
    @ToString.Exclude private final CommandDefinition commandDefinition;
    private final Parameter parameter;

    public static List<ArgumentDefinition> of(CommandDefinition commandDefinition, CommandContainerService commandContainerService) {
        final List<ArgumentDefinition> argumentDefinitions = new ArrayList<>();

        Parameter[] parameters = commandDefinition.getCommandMethod().getParameters();
        for (int i = 0; i < parameters.length; i++) {
            boolean isLastIteration = i >= parameters.length - 1;

            Parameter parameter = parameters[i];
            final Argument annotation = parameter.getAnnotation(Argument.class);

            String name = annotation != null ? annotation.name() : parameter.getType().getSimpleName();
            if (name.isEmpty()) name = parameter.getType().getSimpleName();
            boolean isResponseArgument = !CommandResponse.class.isAssignableFrom(parameter.getType());
            boolean isTail = commandDefinition.getFlagsSet().contains(DefinitionFlag.TAIL_ARG) && isLastIteration;

            //region Setting the processor
            ArgumentProcessor processor = null;

            if (annotation != null) {
                processor = commandContainerService.getArgumentProcessorByClass(annotation.processorClass());
            }

            if (processor == null) {
                processor = commandContainerService.getDefaultArgumentProcessor();
            }
            //endregion

            ArgumentDefinition argumentDefinition = new ArgumentDefinition(name, isResponseArgument, isTail, processor, commandDefinition, parameter);

            argumentDefinitions.add(argumentDefinition);
        }

        return ImmutableList.copyOf(argumentDefinitions);
    }

//    public ArgumentDefinition(boolean isResponseArgument, @Nullable Argument argumentAnnotation, boolean isTail) {
//        this.isResponseArgument = isResponseArgument;
//        @Nullable Argument argumentAnnotation1 = argumentAnnotation;
//        this.isTail = isTail;
//        this.name = argumentAnnotation != null && !argumentAnnotation.name().isEmpty() ? argumentAnnotation.name() : type.getSimpleName();
//    }

//    public DeprecatedArgumentDefinition(Class<?> type, boolean isResponseArgument, @Nullable Argument argumentAnnotation, boolean isTail) {
//        this.type = type;
//        this.isResponseArgument = isResponseArgument;
//        @Nullable Argument argumentAnnotation1 = argumentAnnotation;
//        this.isTail = isTail;
//        this.name = argumentAnnotation != null && !argumentAnnotation.name().isEmpty() ? argumentAnnotation.name() : type.getSimpleName();
//    }
//
//    public String getName() {
//        return isTail ? name + ".." : name;
//    }
//
//    public Class<?> getType() {
//        return type;
//    }
//
//    public boolean isResponseArgument() {
//        return isResponseArgument;
//    }
//
//    public boolean isTail() {
//        return isTail;
//    }


}
