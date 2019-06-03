package com.mayakplay.aclf.pojo;

import com.mayakplay.aclf.annotation.Argument;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Mayakplay on 12.05.2019.
 */
public final class ArgumentDefinition {

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

    public String getName() {
        return isTail ? name + ".." : name;
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isParsedFromString() {
        return parsedFromString;
    }

    public boolean isTail() {
        return isTail;
    }
}