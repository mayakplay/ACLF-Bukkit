package com.mayakplay.aclf.processor;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.PriorityOrdered;

public class DeprecatedCommandControllerRegistererBeanPostProcessor implements BeanPostProcessor, PriorityOrdered {

    private static final String COMMAND_NAME_REGEX = "(?:[a-z]+)|";

//    private static final Map<String, Map<String, C>> commandDefinitionsContainer = new LinkedHashMap<>();
//
//    @Override
//    public Object postProcessBeforeInitialization(@NotNull Object bean, String beanName) throws BeansException {
//        System.out.println(bean.getClass().getSimpleName());
//        CommandController controllerAnnotation = bean.getClass().getAnnotation(CommandController.class);
//
//        if (controllerAnnotation == null) return bean;
//
//
//        //region Mapping setting
//        for (Method method : bean.getClass().getDeclaredMethods()) {
//            ChatMapping chatMappingAnnotation = method.getAnnotation(ChatMapping.class);
//            ChannelMapping channelMappingAnnotation = method.getAnnotation(ChannelMapping.class);
//            CommandMapping bothMappingAnnotation = method.getAnnotation(CommandMapping.class);
//
//            String commandName = "";//controllerAnnotation.value();
//            String subCommandName = getSubCommandNameThrowsIfAnnotationDuplication(
//                    chatMappingAnnotation, channelMappingAnnotation, bothMappingAnnotation);
//
//            boolean chatOnlyFlag = chatMappingAnnotation != null;
//            boolean channelOnlyFlag = channelMappingAnnotation != null;
//
//            //region Command and sub command names checking (Contains throw)
//            //regex checking
//            if (!commandName.matches(COMMAND_NAME_REGEX) || !subCommandName.matches(COMMAND_NAME_REGEX)) {
//                String exceptionString = "Command and sub command names must have [a-z] or blank name! [" + commandName + " " + subCommandName + "]";
//                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + exceptionString);
//                throw new ACLFCriticalException(exceptionString);
//            }
//
//            //Check for both is not empty
//            if (commandName.isEmpty() && subCommandName.isEmpty()) {
//                String exceptionString = "Command and sub command names cannot be empty at the same time";
//                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + exceptionString);
//                throw new ACLFCriticalException(exceptionString);
//            }
//
//            //Swap if command name is empty
//            if (commandName.isEmpty()) {
//                commandName = subCommandName;
//                subCommandName = "";
//            }
//            //endregion
//
//            DeprecatedCommandDefinition commandDefinition = new DeprecatedCommandDefinition(
//                    commandName, subCommandName, chatOnlyFlag, channelOnlyFlag, bean.getClass(), method);
//
//
//            registerCommand(commandDefinition);
//            StaticUtils.registerCommand(commandName);
//        }
//        //endregion
//
//        return bean;
//    }
//
//    private void registerCommand(DeprecatedCommandDefinition commandDefinition) {
//        Bukkit.getConsoleSender().sendMessage(commandDefinition.toString());
//        String command = commandDefinition.getControllerName();
//        String subCommand = commandDefinition.getSubCommandName();
//
//        Map<String, DeprecatedCommandDefinition> stringRegisteredCommandMap = commandDefinitionsContainer.computeIfAbsent(command, k -> new HashMap<>());
//
//        //region Command existence checking
//        if (stringRegisteredCommandMap.containsKey(subCommand)) {
//            String exceptionString = "Command [" + command + ":" + subCommand + "] is already exists!";
//            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + exceptionString);
//            throw new ACLFCriticalException(exceptionString);
//        }
//        //endregion
//
//
//        stringRegisteredCommandMap.put(subCommand, commandDefinition);
//    }
//
//    private String getSubCommandNameThrowsIfAnnotationDuplication(
//            ChatMapping chatMappingAnnotation,
//            ChannelMapping channelMappingAnnotation,
//            CommandMapping bothMappingAnnotation
//    ) throws ACLFCriticalException {
//        boolean isChatMapping = chatMappingAnnotation != null;
//        boolean isChannelMapping = channelMappingAnnotation != null;
//        boolean isBothMapping = bothMappingAnnotation != null;
//
//        int countTrues = StaticUtils.countTrues(isChatMapping, isBothMapping, isChannelMapping);
//
//        if (countTrues > 1)   throw new ACLFCriticalException("Not allowed to use more than one mapping annotation! Select between [@ChatMapping, @ChannelMapping or @BothMapping].");
//        if (isChatMapping)    return chatMappingAnnotation.value();
//        if (isChannelMapping) return channelMappingAnnotation.value();
//        if (isBothMapping)    return bothMappingAnnotation.value();
//
//        throw new ACLFCriticalException("Something goes wrong while command registration");
//
//    }
//
//    public static Map<String, Map<String, DeprecatedCommandDefinition>> getCommandDefinitionsContainer() {
//        return StaticUtils.getImmutableMapWithInnerMapInValue(commandDefinitionsContainer);
//    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
