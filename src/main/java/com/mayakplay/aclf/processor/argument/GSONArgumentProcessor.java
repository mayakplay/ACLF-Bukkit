package com.mayakplay.aclf.processor.argument;

import com.google.gson.Gson;
import com.mayakplay.aclf.exception.ACLFException;
import com.mayakplay.aclf.stereotype.ArgumentProcessor;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 25.06.2019.
 */
@Component
@AllArgsConstructor
public class GSONArgumentProcessor implements ArgumentProcessor {

    private final Gson gson;

    @Override
    public Object parse(@NotNull String argumentMessage, Type returnType) throws ACLFException {
        try {
            return gson.fromJson("\"" + argumentMessage + "\"", returnType);
        } catch (Exception ignored) {
            return null;
        }
    }

}