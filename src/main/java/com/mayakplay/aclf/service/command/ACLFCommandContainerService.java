package com.mayakplay.aclf.service.command;

import com.google.common.collect.ImmutableList;
import com.mayakplay.aclf.definition.CommandControllerDefinition;
import com.mayakplay.aclf.definition.CommandDefinition;
import com.mayakplay.aclf.event.ControllersClassesScanFinishedEvent;
import com.mayakplay.aclf.exception.ACLFCriticalException;
import com.mayakplay.aclf.processor.argument.GSONArgumentProcessor;
import com.mayakplay.aclf.stereotype.ArgumentProcessor;
import com.mayakplay.aclf.util.StaticUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 21.06.2019.
 */
@Component
public class ACLFCommandContainerService implements CommandContainerService, ApplicationListener<ControllersClassesScanFinishedEvent> {

    private static final String COMMAND_NAME_REGEX = "(?:[a-z]+)|";

    //region Construction
    /**
     * &lt;Full command name, Command definition&gt;
     */
    @NotNull private final Map<String, CommandDefinition> commandDefinitionAssociationsMap = new HashMap<>();

    /**
     * &lt;First command name, Command definitions list&gt;
     */
    @NotNull private final Map<String, List<CommandDefinition>> commandDefinitionsListAssociationsMap = new HashMap<>();

    private final HashMap<Class<? extends ArgumentProcessor>, ArgumentProcessor> argumentProcessorHashMap = new HashMap<>();
    private final GSONArgumentProcessor defaultProcessor;

    /**
     * @param argumentProcessorList fills {@link #argumentProcessorHashMap} from {@link ApplicationContext} beans
     * @param defaultProcessor getting the {@link GSONArgumentProcessor} instance
     */
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
    //endregion

    //region CommandContainerService implementation
    @Override
    @Nullable
    public CommandDefinition getDefinition(@NotNull String commandName, @Nullable String subCommandName) {
        String fullCommandName = commandName;
        if (subCommandName != null) fullCommandName += ":" + subCommandName;

        return commandDefinitionAssociationsMap.get(fullCommandName.toLowerCase());
    }

    @Override
    @NotNull
    public List<CommandDefinition> getDefinitionsByFirstCommandName(@NotNull String firstCommandName) {
        List<CommandDefinition> commandDefinitions = commandDefinitionsListAssociationsMap.get(firstCommandName);

        return commandDefinitions == null ? Collections.emptyList() : ImmutableList.copyOf(commandDefinitions);
    }

    @Override
    public void onApplicationEvent(@NotNull ControllersClassesScanFinishedEvent event) {
        Map<String, Class<?>> controllersClassesMap = event.getSource();

        for (Map.Entry<String, Class<?>> entry : controllersClassesMap.entrySet()) {
            Class<?> controllerClass = entry.getValue();
            String controllerBeanName = entry.getKey();

            CommandControllerDefinition controllerDefinition = CommandControllerDefinition.of(controllerClass, controllerBeanName, this);

            for (CommandDefinition definition : controllerDefinition.getCommandDefinitionsList()) {
                checkRegex(definition.getFirstCommandName(), definition.getSecondCommandName(), controllerDefinition.getControllerClass().getName());

                //region throw if exists
                if (commandDefinitionAssociationsMap.containsKey(definition.getCommandName()))
                    throw new ACLFCriticalException("Command with name \"" + ChatColor.AQUA + definition.getCommandName() + "\" already exists!");
                //endregion

                //full command name association
                commandDefinitionAssociationsMap.put(definition.getCommandName(), definition);

                //region first command name association
                List<CommandDefinition> commandDefinitions =
                        commandDefinitionsListAssociationsMap.computeIfAbsent(definition.getFirstCommandName(), s -> new ArrayList<>());
                commandDefinitions.add(definition);
                //endregion

                //region avoid bukkit commands registerer (plguin.yml)
                StaticUtils.registerCommand(definition.getFirstCommandName());
                //endregion
            }
        }

        for (Map.Entry<String, CommandDefinition> entry : commandDefinitionAssociationsMap.entrySet()) {
            System.out.println(entry.getKey() + entry.getValue());

            String description = entry.getValue().getCommandDescriptionScanner().getDescription();
            if (description != null) {
                for (String s : WordUtils.wrap(description, 40, null, true).split("\n")) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "  " + s);
                }
            }
        }
    }

    @Nullable
    @Override
    public ArgumentProcessor getArgumentProcessorByClass(Class<? extends ArgumentProcessor> processorClass) {
        return argumentProcessorHashMap.get(processorClass);
    }

    @Override
    @NotNull
    public ArgumentProcessor getDefaultArgumentProcessor() {
        return defaultProcessor;
    }
    //endregion

    //region Util
    private static void checkRegex(@NotNull String commandName, @Nullable String subCommandName, String className) {
        boolean mistake = false;

        if (!commandName.matches(COMMAND_NAME_REGEX)) mistake = true;
        if (subCommandName != null && !subCommandName.matches(COMMAND_NAME_REGEX)) mistake = true;

        if (mistake) {
            String message = "Check [" + commandName + " " + subCommandName + "] command name regex " +
                    "in \"" + ChatColor.WHITE + className + ChatColor.RED + "\" It must be ([a-z] or empty)";
            throw new ACLFCriticalException(message);
        }

    }
    //endregion

}