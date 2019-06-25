package com.mayakplay.aclf.definition;

import com.mayakplay.aclf.annotation.CommandMapping;
import com.mayakplay.aclf.annotation.OpsOnly;
import com.mayakplay.aclf.annotation.Permitted;
import com.mayakplay.aclf.annotation.TailArgumentCommand;
import com.mayakplay.aclf.type.DefinitionFlag;
import com.mayakplay.aclf.type.MappingAccess;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 15.06.2019.
 */
@ToString
@EqualsAndHashCode(of = {"commandName", "commandControllerDefinition"})
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandDefinition {

    @NotNull
    @Getter
    private final String commandName;

    @NotNull
    private final EnumSet<DefinitionFlag> flags;

    @NotNull
    private final Method commandMethod;

    @NotNull
    @ToString.Exclude private final CommandControllerDefinition commandControllerDefinition;

    @NotNull
    private final Set<String> permissionSet;

    @NotNull
    private final MappingAccess mappingAccess;

    @NotNull
    private final CommandDescriptionScanner commandDescriptionScanner;

    @SuppressWarnings("Duplicates")
    public static CommandDefinition of(Method commandMethod, CommandControllerDefinition commandControllerDefinition) {
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

        CommandDescriptionScanner commandDescriptionScanner = new CommandDescriptionScanner(commandControllerDefinition.getControllerClass(), commandMethod);

        return new CommandDefinition(commandName, flags, commandMethod, commandControllerDefinition, permissionsSet, mappingAccess, commandDescriptionScanner);
    }

}