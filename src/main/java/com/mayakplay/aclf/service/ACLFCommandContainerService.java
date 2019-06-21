package com.mayakplay.aclf.service;

import com.mayakplay.aclf.definition.CommandDefinition;
import com.mayakplay.aclf.service.interfaces.CommandContainerService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 21.06.2019.
 */
@Component
public class ACLFCommandContainerService implements CommandContainerService {


    public ACLFCommandContainerService() {

    }

    @Override
    public boolean containsCommand(@NotNull String commandName, @Nullable String subCommandName) {
        return false;
    }

    @Override
    public CommandDefinition getDefinition(@NotNull String commandName, @Nullable String subCommandName) {
        return null;
    }
}