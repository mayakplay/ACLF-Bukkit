package com.mayakplay.aclf.service.command;

import com.mayakplay.aclf.definition.CommandDefinition;
import com.mayakplay.aclf.type.ArgumentMistakeType;
import com.mayakplay.aclf.type.CommandProcessOutput;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 25.06.2019.
 */
public interface CommandMessagingService {

    void sendResponseMessage(@NotNull  CommandProcessOutput commandProcessOutput,
                             @NotNull  CommandDefinition nullable,
                             @NotNull  CommandSender sender,
                             @Nullable String exceptionMessage,
                             @Nullable List<ArgumentMistakeType> mistakeTypes);

    default void sendResponseMessage(@NotNull  CommandProcessOutput commandProcessOutput,
                                     @NotNull  CommandDefinition nullable,
                                     @NotNull  CommandSender sender) {
        sendResponseMessage(commandProcessOutput, nullable, sender, null, null);
    }

    default void sendResponseMessage(@NotNull  CommandProcessOutput commandProcessOutput,
                                     @NotNull  CommandDefinition nullable,
                                     @NotNull  CommandSender sender,
                                     @Nullable String exceptionMessage) {
        sendResponseMessage(commandProcessOutput, nullable, sender, exceptionMessage, null);
    }

    default void sendResponseMessage(@NotNull  CommandProcessOutput commandProcessOutput,
                                     @NotNull  CommandDefinition nullable,
                                     @NotNull  CommandSender sender,
                                     @Nullable List<ArgumentMistakeType> mistakeTypes) {
        sendResponseMessage(commandProcessOutput, nullable, sender, null, mistakeTypes);
    }

    String getUsageMessage(@NotNull CommandDefinition commandDefinition,
                           @Nullable List<ArgumentMistakeType> argumentMistakeTypes);

    default String getUsageMessage(@NotNull CommandDefinition commandDefinition) {
        return getUsageMessage(commandDefinition, null);
    }

}