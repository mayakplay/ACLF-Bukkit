package com.mayakplay.aclf.definition;

import com.google.common.collect.ImmutableList;
import com.mayakplay.aclf.annotation.CommandMapping;
import com.mayakplay.aclf.annotation.OpsOnly;
import com.mayakplay.aclf.annotation.Permitted;
import com.mayakplay.aclf.annotation.TailArgumentCommand;
import com.mayakplay.aclf.type.DefinitionFlag;
import com.mayakplay.aclf.type.MappingAccess;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 15.06.2019.
 */
@Getter
@ToString
@EqualsAndHashCode(of = "controllerName")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandControllerDefinition {

    @Nullable
    private final String controllerName;

    @NotNull
    private final EnumSet<DefinitionFlag> flags;

    @NotNull
    private final Class<?> controllerClass;

    @NotNull
    private final String controllerBeanName;

    @NotNull
    private final List<CommandDefinition> commandDefinitionsList = new ArrayList<>();

    @NotNull
    private final MappingAccess mappingAccess;

    @NotNull
    private final List<String> permissionsList;

    @SuppressWarnings("Duplicates")
    public static CommandControllerDefinition of(@NotNull Class<?> controllerClass, @NotNull String controllerBeanName) {
        //region Бредятина всякая
        final CommandMapping      commandMapping = AnnotatedElementUtils.getMergedAnnotation(controllerClass, CommandMapping.class);
        final Permitted           permitted = AnnotatedElementUtils.getMergedAnnotation(controllerClass, Permitted.class);
        final OpsOnly             opsOnly = AnnotatedElementUtils.getMergedAnnotation(controllerClass, OpsOnly.class);
        final TailArgumentCommand tailArgumentCommand = AnnotatedElementUtils.getMergedAnnotation(controllerClass, TailArgumentCommand.class);

        final String commandName                             = commandMapping != null ? commandMapping.value() : null;
        final EnumSet<DefinitionFlag> flags                  = EnumSet.noneOf(DefinitionFlag.class);
        final MappingAccess mappingAccess                    = commandMapping != null ? commandMapping.privacy() : MappingAccess.BOTH;
        final List<String> permissionsList                   = permitted != null ? ImmutableList.copyOf(permitted.value()) : Collections.emptyList();

        if (opsOnly != null)                             flags.add(DefinitionFlag.OPS_ONLY);
        if (tailArgumentCommand != null)                 flags.add(DefinitionFlag.TAIL_ARG);
        if (mappingAccess.equals(MappingAccess.CHAT))    flags.add(DefinitionFlag.CHAT_ONLY);
        if (mappingAccess.equals(MappingAccess.CHANNEL)) flags.add(DefinitionFlag.CHANNEL_ONLY);
        //endregion

        CommandControllerDefinition commandControllerDefinition = new CommandControllerDefinition(commandName, flags, controllerClass, controllerBeanName, mappingAccess, permissionsList);

        for (Method method : controllerClass.getDeclaredMethods()) {
            CommandDefinition of = CommandDefinition.of(method, commandControllerDefinition);

            if (of != null)
                commandControllerDefinition.commandDefinitionsList.add(of);
        }

        return commandControllerDefinition;
    }

    @NotNull
    public List<CommandDefinition> getCommandDefinitionsList() {
        return ImmutableList.copyOf(commandDefinitionsList);
    }
}