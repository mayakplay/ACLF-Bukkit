package com.mayakplay.aclf.service;

import com.mayakplay.aclf.definition.CommandControllerDefinition;
import com.mayakplay.aclf.definition.CommandDefinition;
import com.mayakplay.aclf.event.ControllersClassesScanFinishedEvent;
import com.mayakplay.aclf.exception.ACLFCriticalException;
import com.mayakplay.aclf.processor.argument.GSONArgumentProcessor;
import com.mayakplay.aclf.service.interfaces.CommandContainerService;
import com.mayakplay.aclf.stereotype.ArgumentProcessor;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 21.06.2019.
 */
@Component
public class ACLFCommandContainerService implements CommandContainerService, ApplicationListener<ControllersClassesScanFinishedEvent> {

    private static final String COMMAND_NAME_REGEX = "(?:[a-z]+)|";

    @NotNull
    private final Map<String, CommandDefinition> commandDefinitionAssociationsMap = new HashMap<>();

    private final HashMap<Class<? extends ArgumentProcessor>, ArgumentProcessor> argumentProcessorHashMap = new HashMap<>();
    private final GSONArgumentProcessor defaultProcessor;

    @Autowired
    public ACLFCommandContainerService(List<ArgumentProcessor> argumentProcessorList, GSONArgumentProcessor defaultProcessor) {
        for (ArgumentProcessor processor : argumentProcessorList) {
            System.out.println(processor.getClass().getName());
        }

        this.defaultProcessor = defaultProcessor;
        for (ArgumentProcessor processor : argumentProcessorList) {
            argumentProcessorHashMap.put(processor.getClass(), processor);
        }
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

            CommandControllerDefinition controllerDefinition = CommandControllerDefinition.of(controllerClass, controllerBeanName, this);

            for (CommandDefinition definition : controllerDefinition.getCommandDefinitionsList()) {
                checkRegex(controllerDefinition.getControllerName(), definition.getCommandDefinitionName(), controllerDefinition.getControllerClass().getName());
                String fullCommandName = controllerDefinition.getControllerName() + ":" + definition.getCommandDefinitionName();
                commandDefinitionAssociationsMap.put(fullCommandName, definition);
            }
        }

        for (Map.Entry<String, CommandDefinition> entry : commandDefinitionAssociationsMap.entrySet()) {
            System.out.println(entry.getKey() + entry.getValue());
        }
    }

    @Nullable
    @Override
    public ArgumentProcessor getArgumentProcessorByClass(Class<? extends ArgumentProcessor> processorClass) {
        return argumentProcessorHashMap.get(processorClass);
    }

    @NotNull
    public ArgumentProcessor getDefaultArgumentProcessor() {
        return defaultProcessor;
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