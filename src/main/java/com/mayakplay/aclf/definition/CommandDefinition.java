package com.mayakplay.aclf.definition;

import com.mayakplay.aclf.annotation.CommandMapping;
import com.mayakplay.aclf.annotation.Permitted;
import com.mayakplay.aclf.type.DefinitionFlag;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 15.06.2019.
 */
@Getter
public class CommandDefinition implements AnnotatedElement {

    @NotNull
    private final String commandName;

    @NotNull
    private final EnumSet<DefinitionFlag> flags;

    @NotNull
    private final Method commandMethod;

    @NotNull
    private final CommandControllerDefinition commandControllerDefinition;

    @NotNull
    private final Set<String> permissionSet;

    private CommandDefinition(@NotNull String commandName, @NotNull Method commandMethod,
                              @NotNull CommandControllerDefinition commandControllerDefinition, DefinitionFlag... flags) {
        this.commandName = commandName;
        this.commandMethod = commandMethod;
        this.flags = EnumSet.allOf(DefinitionFlag.class);
        this.flags.addAll(Arrays.asList(flags));
        this.commandControllerDefinition = commandControllerDefinition;
        this.permissionSet = new HashSet<>();

        fillPermissions();

        CommandDescriptionScanner scanner = new CommandDescriptionScanner(this);
        scanner.scan();
    }

    private void fillPermissions() {
        Permitted controllerAnnotation = commandControllerDefinition.getAnnotation(Permitted.class);
        Permitted annotation = this.getAnnotation(Permitted.class);

        if (controllerAnnotation != null) permissionSet.addAll(Arrays.asList(controllerAnnotation.value()));
        if (annotation != null) permissionSet.addAll(Arrays.asList(annotation.value()));
    }

    @Nullable
    public static CommandDefinition of(@NotNull Method method, @NotNull CommandControllerDefinition controllerDefinition) {
        CommandMapping mergedAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, CommandMapping.class);
        if (mergedAnnotation == null) {
            return null;
        } else {
            String commandName = mergedAnnotation.value();
            return new CommandDefinition(commandName, method, controllerDefinition);
        }
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return AnnotatedElementUtils.findMergedAnnotation(commandMethod, annotationClass);
    }

    @Override
    public Annotation[] getAnnotations() {
        return commandMethod.getAnnotations();
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return commandMethod.getDeclaredAnnotations();
    }

    public String getControllerCommandName() {
        return commandControllerDefinition.getCommandName();
    }

    @NotNull
    public Set<String> getPermissionSet() {
        return permissionSet;
    }

    public boolean hasOpsOnlyFlag() {
        return flags.contains(DefinitionFlag.OPS_ONLY);
    }

    public boolean hasPlayerOnlyFlag() {
        return flags.contains(DefinitionFlag.PLAYER_SENDER_ONLY);
    }

    public boolean hasConsoleOnlyFlag() {
        return flags.contains(DefinitionFlag.OPS_ONLY);
    }

    public boolean hasChatOnlyFlag() {
        return flags.contains(DefinitionFlag.CHAT_ONLY);
    }

    public boolean hasChannelOnlyFlag() {
        return flags.contains(DefinitionFlag.CHANNEL_ONLY);
    }
}