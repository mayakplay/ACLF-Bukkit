package com.mayakplay.aclf.stereotype;

import com.mayakplay.aclf.exception.ACLFException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 09.06.2019.
 */
public interface ArgumentProcessor<T> {

    @Nullable
    T parse(@NotNull String argumentMessage, Type returnType) throws ACLFException;

}