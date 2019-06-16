package com.mayakplay.aclf.definition;

import com.mayakplay.aclf.annotation.Argument;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 17.06.2019.
 */
@Getter
public class ArgumentDefinition {

    private final String name;
    private final Class<?> type;
    private final boolean parsedFromString;
    private final boolean isTail;

    public ArgumentDefinition(Class<?> type, boolean parsedFromString, @Nullable Argument argumentAnnotation, boolean isTail) {
        this.type = type;
        this.parsedFromString = parsedFromString;
        @Nullable Argument argumentAnnotation1 = argumentAnnotation;
        this.isTail = isTail;
        this.name = argumentAnnotation != null && !argumentAnnotation.name().isEmpty() ? argumentAnnotation.name() : type.getSimpleName();
    }


}
