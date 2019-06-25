package com.mayakplay.aclf.processor.argument;

import com.mayakplay.aclf.stereotype.ArgumentProcessor;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 26.06.2019.
 */
@Component
@Deprecated
@AllArgsConstructor
public class JokeProcessor implements ArgumentProcessor {

    private final GSONArgumentProcessor gsonArgumentProcessor;

    @Nullable
    @Override
    public Object parse(@NotNull String argumentMessage, Type returnType) throws Throwable {
        if (argumentMessage.equalsIgnoreCase("joke"))
            return "HTML is programming language";
        return gsonArgumentProcessor.parse(argumentMessage, returnType);
    }
}
