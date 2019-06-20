package com.mayakplay.aclf.definition;

import com.mayakplay.aclf.annotation.Documented;
import com.mayakplay.aclf.type.DefinitionFlag;
import lombok.Getter;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 15.06.2019.
 */
@Getter
public class CommandDefinition implements Definition, AnnotatedElement {

    private boolean built = false;

    private String commandName;

    private final EnumSet<DefinitionFlag> flags;
    private final HashMap<Class<? extends Annotation>, Annotation> definedAnnotations;
    private final CommandControllerDefinition commandControllerDefinition;
    private final Method commandMethod;

    public CommandDefinition(CommandControllerDefinition commandControllerDefinition, Method commandMethod, DefinitionFlag... flags) {
        this.commandControllerDefinition = commandControllerDefinition;
        this.commandMethod = commandMethod;
        this.definedAnnotations = new HashMap<>();
        this.flags = EnumSet.allOf(DefinitionFlag.class);
        this.flags.addAll(Arrays.asList(flags));

        build();
        CommandDescriptionScanner scanner = new CommandDescriptionScanner(this);
        scanner.scan();
    }

    public void build() {
        if (built) return;

        Annotation[] controllerAnnotations = commandControllerDefinition.getControllerClass().getDeclaredAnnotations();
        Annotation[] methodAnnotations = commandMethod.getDeclaredAnnotations();

        Arrays.stream(controllerAnnotations).forEach(annotation -> definedAnnotations.put(annotation.annotationType(), annotation));
        Arrays.stream(methodAnnotations).forEach(annotation -> definedAnnotations.put(annotation.annotationType(), annotation));

        built = true;
    }

    @Override
    public boolean isBuilt() {
        return built;
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        Objects.requireNonNull(annotationType);
        return AnnotatedElementUtils.findMergedAnnotation(this, annotationType);
    }

    @Override
    public Annotation[] getAnnotations() {
        return definedAnnotations.values().toArray(new Annotation[0]);
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return definedAnnotations.values().toArray(new Annotation[0]);
    }

    //region inner
    /**
     * Class needed to scan values of {@link Documented} annotation.
     */
    private class CommandDescriptionScanner {

        private CommandDefinition definition;

        private boolean hasDisplayedInHelp;

        private String opsOnlyMessage;
        private String noPermissionsMessage;
        private String consoleOnlyMessage;
        private String playersOnlyMessage;
        private String usageMessage;
        private String chatOnlyMessage;
        private String channelOnlyMessage;

        CommandDescriptionScanner(CommandDefinition definition) {
            this.definition = definition;
        }

        void scan() {

            this.opsOnlyMessage = getMessage("", "");
        }

        private String getMessage(String annotationMessage, String defaultMessage) {
            return null;
        }


    }
    //endregion

}