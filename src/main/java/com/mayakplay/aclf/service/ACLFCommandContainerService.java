package com.mayakplay.aclf.service;

import com.mayakplay.aclf.definition.CommandControllerDefinition;
import com.mayakplay.aclf.definition.CommandDefinition;
import com.mayakplay.aclf.event.ControllersClassesScanFinishedEvent;
import com.mayakplay.aclf.exception.ACLFCriticalException;
import com.mayakplay.aclf.service.interfaces.CommandContainerService;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 21.06.2019.
 */
@Component
public class ACLFCommandContainerService implements CommandContainerService, ApplicationListener<ControllersClassesScanFinishedEvent> {

    static final String COMMAND_NAME_REGEX = "(?:[a-z]+)|";

    @NotNull
    private final Map<String, CommandDefinition> commandDefinitionAssociationsMap = new HashMap<>();

    @Override
    public boolean containsCommand(@NotNull String commandName, @Nullable String subCommandName) {
        return commandDefinitionAssociationsMap.containsKey(commandName + ":" + subCommandName);
    }

    @Override
    @Nullable
    public CommandDefinition getDefinition(@NotNull String commandName, @Nullable String subCommandName) {
        return commandDefinitionAssociationsMap.get(commandName + ":" + subCommandName);
    }

    @Override
    public void onApplicationEvent(@NotNull ControllersClassesScanFinishedEvent event) {
        Map<String, Class<?>> controllersClassesMap = event.getSource();

        for (Map.Entry<String, Class<?>> entry : controllersClassesMap.entrySet()) {
            Class<?> controllerClass = entry.getValue();
            String controllerBeanName = entry.getKey();

            CommandControllerDefinition controllerDefinition = CommandControllerDefinition.of(controllerClass, controllerBeanName);

            for (CommandDefinition definition : controllerDefinition.getCommandDefinitionsList()) {
                checkRegex(controllerDefinition.getControllerName(), definition.getCommandName(), controllerDefinition.getControllerClass().getName());
                String fullCommandName = controllerDefinition.getControllerName() + ":" + definition.getCommandName();
                commandDefinitionAssociationsMap.put(fullCommandName, definition);
            }
        }

        for (Map.Entry<String, CommandDefinition> entry : commandDefinitionAssociationsMap.entrySet()) {
            System.out.println(entry.getKey() + entry.getValue());
        }
    }

    private void checkRegex(String commandName, String subCommandName, String className) {
        if (subCommandName.isEmpty()) subCommandName = "{BLANK}";
        if (!commandName.matches(COMMAND_NAME_REGEX) || !subCommandName.matches(COMMAND_NAME_REGEX)) {
            String message = "Check [" + commandName + " " + subCommandName + "] command name regex " +
                    "in \"" + ChatColor.WHITE + className + ChatColor.RED + "\" It must be ([a-z] or empty)";
            throw new ACLFCriticalException(message);
        }
    }
}