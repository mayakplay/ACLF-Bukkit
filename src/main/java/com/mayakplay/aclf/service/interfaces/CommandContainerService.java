package com.mayakplay.aclf.service.interfaces;

import com.mayakplay.aclf.definition.CommandDefinition;
import com.mayakplay.aclf.stereotype.ArgumentProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 21.06.2019.
 */
public interface CommandContainerService {

    @Nullable
    CommandDefinition getDefinition(@NotNull String commandName, @Nullable String subCommandName);

    default boolean containsCommand(@NotNull String commandName, @Nullable String subCommandName) {
        return getDefinition(commandName, subCommandName) != null;
    }

    @Nullable
    default CommandDefinition getDefinitionByMessage(@NotNull String message) {
        String[] split = message.split(" ");

        return split.length < 1 ? null :
                split.length == 1 ? getDefinition(split[0], null) : getDefinition(split[0], split[1]);
    }

    ArgumentProcessor getArgumentProcessorByClass(Class<? extends ArgumentProcessor> processorClass);

    ArgumentProcessor getDefaultArgumentProcessor();

}