package com.mayakplay.aclf.service.command;

import com.mayakplay.aclf.definition.CommandDefinition;
import com.mayakplay.aclf.stereotype.ArgumentProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 21.06.2019.
 */
public interface CommandContainerService {

    @Nullable
    CommandDefinition getDefinition(@NotNull String commandName, @Nullable String subCommandName);

    @NotNull
    List<CommandDefinition> getDefinitionsByFirstCommandName(@NotNull String firstCommandName);

    default boolean containsCommand(@NotNull String commandName, @Nullable String subCommandName) {
        return getDefinition(commandName, subCommandName) != null;
    }

    @Nullable
    default CommandDefinition getDefinitionByMessage(@NotNull String message) {
        String[] split = message.split(ACLFCommandProcessingService.COMMAND_REGEX);

        CommandDefinition commandDefinition = null;

        if (split.length > 1) {
            commandDefinition                                = getDefinition(split[0], split[1]);
            if (commandDefinition == null) commandDefinition = getDefinition(split[0], null);
        }

        if (split.length == 1) {
            commandDefinition = getDefinition(split[0], null);
        }

        return commandDefinition;
    }

    ArgumentProcessor getArgumentProcessorByClass(Class<? extends ArgumentProcessor> processorClass);

    ArgumentProcessor getDefaultArgumentProcessor();

}