package com.mayakplay.aclf.service.interfaces;

import com.mayakplay.aclf.definition.CommandDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 21.06.2019.
 */
public interface CommandContainerService {

    boolean containsCommand(@NotNull String commandName, @Nullable String subCommandName);

    CommandDefinition getDefinition(@NotNull String commandName, @Nullable String subCommandName);

}