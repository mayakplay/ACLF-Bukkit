package com.mayakplay.aclf.definition;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mayakplay.aclf.annotation.CommandMapping;
import com.mayakplay.aclf.annotation.OpsOnly;
import com.mayakplay.aclf.annotation.Permitted;
import com.mayakplay.aclf.annotation.TailArgumentCommand;
import com.mayakplay.aclf.exception.ACLFCriticalException;
import com.mayakplay.aclf.service.interfaces.CommandContainerService;
import com.mayakplay.aclf.type.DefinitionFlag;
import com.mayakplay.aclf.type.MappingAccess;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * idk what to do with this code, but it works
 *
 * @author mayakplay
 * @version 0.0.1
 * @since 15.06.2019.
 */
@EqualsAndHashCode(of = {"commandDefinitionName", "commandControllerDefinition"})
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandDefinition {

    @Nullable
    @Getter
    private final String commandDefinitionName;

    @NotNull
    private final EnumSet<DefinitionFlag> flags;

    @NotNull
    @Getter
    private final Method commandMethod;

    @NotNull
    @ToString.Exclude
    private final CommandControllerDefinition commandControllerDefinition;

    @NotNull
    private final Set<String> permissionSet;

    @NotNull
    private final MappingAccess mappingAccess;

    @NotNull
    @Getter
    private final CommandDescriptionDefinition commandDescriptionScanner;

    @NotNull
    private final List<ArgumentDefinition> argumentDefinitions = new ArrayList<>();

    @SuppressWarnings("Duplicates")
    public static CommandDefinition of(Method commandMethod, CommandControllerDefinition commandControllerDefinition, CommandContainerService commandContainerService) {
        final CommandMapping commandMapping = AnnotatedElementUtils.getMergedAnnotation(commandMethod, CommandMapping.class);
        if (commandMapping == null) return null;
        final Permitted permitted = AnnotatedElementUtils.getMergedAnnotation(commandMethod, Permitted.class);
        final OpsOnly opsOnly = AnnotatedElementUtils.getMergedAnnotation(commandMethod, OpsOnly.class);
        final TailArgumentCommand tailArgumentCommand = AnnotatedElementUtils.getMergedAnnotation(commandMethod, TailArgumentCommand.class);

        final String commandName= commandMapping.value();
        final EnumSet<DefinitionFlag> flags                  = EnumSet.noneOf(DefinitionFlag.class);
        flags.addAll(commandControllerDefinition.getFlags());
        final Set<String> permissionsSet = new HashSet<>(commandControllerDefinition.getPermissionsList());

        final MappingAccess mappingAccess = commandMapping.privacy();

        if (opsOnly != null)                             flags.add(DefinitionFlag.OPS_ONLY);
        if (tailArgumentCommand != null)                 flags.add(DefinitionFlag.TAIL_ARG);
        if (mappingAccess.equals(MappingAccess.CHAT))    flags.add(DefinitionFlag.CHAT_ONLY);
        if (mappingAccess.equals(MappingAccess.CHANNEL)) flags.add(DefinitionFlag.CHANNEL_ONLY);

        if (permitted != null) permissionsSet.addAll(Arrays.asList(permitted.value()));

        CommandDescriptionDefinition commandDescriptionScanner = new CommandDescriptionDefinition(commandControllerDefinition.getControllerClass(), commandMethod);
        CommandDefinition commandDefinition = new CommandDefinition(commandName, flags, commandMethod, commandControllerDefinition, permissionsSet, mappingAccess, commandDescriptionScanner);

        List<ArgumentDefinition> list = ArgumentDefinition.of(commandDefinition, commandContainerService);

        commandDefinition.argumentDefinitions.addAll(list);
        return commandDefinition;
    }

    @NotNull
    public Set<String> getPermissionSet() {
        return ImmutableSet.copyOf(permissionSet);
    }

    @NotNull
    public Set<DefinitionFlag> getFlagsSet() {
        return ImmutableSet.copyOf(flags);
    }

    @NotNull
    public List<ArgumentDefinition> getArgumentDefinitions() {
        return ImmutableList.copyOf(argumentDefinitions);
    }

    /**
     * command of 0 words EXCEPTION xD
     * command of 1 word  "commandName"
     * command of 2 words "commandName:subCommandName"
     */
    @NotNull
    public String getCommandName() {
        String controllerName = commandControllerDefinition.getControllerName();

        if (commandDefinitionName == null && controllerName == null)
            throw new ACLFCriticalException("Command must be named!");

        if (Strings.isNullOrEmpty(commandDefinitionName)) //noinspection ConstantConditions (lie)
            return controllerName;
        if (Strings.isNullOrEmpty(controllerName))
            return commandDefinitionName;

        return controllerName + ":" + commandDefinitionName;
    }

    @NotNull
    public String getFirstCommandName() {
        String controllerName = commandControllerDefinition.getControllerName();

        if (controllerName == null) return getCommandName();
        return controllerName;
    }

    @Nullable
    public String getSecondCommandName() {
        String controllerName = commandControllerDefinition.getControllerName();

        if (controllerName == null || commandDefinitionName == null)
            return null;
        else
            return commandDefinitionName;
    }

    @NotNull
    public Class<?> getControllerClass() {
        return commandControllerDefinition.getControllerClass();
    }

    public boolean isForChatOnly() {
        return getFlagsSet().contains(DefinitionFlag.CHAT_ONLY) &&
                !getFlagsSet().contains(DefinitionFlag.CHANNEL_ONLY);
    }

    public boolean isForChannelOnly() {
        return getFlagsSet().contains(DefinitionFlag.CHANNEL_ONLY) &&
                !getFlagsSet().contains(DefinitionFlag.CHAT_ONLY);
    }

}