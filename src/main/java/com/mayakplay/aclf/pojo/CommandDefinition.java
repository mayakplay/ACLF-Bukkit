package com.mayakplay.aclf.pojo;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mayakplay.aclf.annotation.Argument;
import com.mayakplay.aclf.annotation.OpsOnly;
import com.mayakplay.aclf.annotation.Permitted;
import com.mayakplay.aclf.annotation.TailArgumentCommand;
import com.mayakplay.aclf.exception.ACLFCriticalException;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.YELLOW;

public final class CommandDefinition {

    @NotNull
    private final String commandName;

    @NotNull
    private final String subCommandName;

    @NotNull
    private final Set<String> permissionSet;

    @NotNull
    private final Object controllerBean;

    @NotNull
    private final Method method;

    @NotNull
    private final CommandDescriptionDefinition commandDescriptionDefinition;

    private final boolean chatOnlyFlag;
    private final boolean channelOnlyFlag;

    private boolean playerSenderOnlyFlag;
    private boolean consoleSenderOnlyFlag;
    private boolean opsOnlyFlag;

    private final boolean tailArgFlag;

    private int argumentsCount;

    @NotNull
    private final List<ArgumentDefinition> argumentDefinitionList;

    public CommandDefinition(@NotNull String commandName, @NotNull String subCommandName,
                             boolean chatOnlyFlag, boolean channelOnlyFlag,
                             @NotNull Object controllerBean, @NotNull Method method) {
        this.commandName = commandName;
        this.subCommandName = subCommandName;
        this.controllerBean = controllerBean;

        this.chatOnlyFlag = chatOnlyFlag;
        this.channelOnlyFlag = channelOnlyFlag;

        this.method = method;
        this.method.setAccessible(true);

        this.permissionSet = new HashSet<>();
        this.argumentDefinitionList = new ArrayList<>();

        this.tailArgFlag = method.isAnnotationPresent(TailArgumentCommand.class);

        scanMethodForDefinitions();
        scanMethodArguments();
        this.commandDescriptionDefinition = new CommandDescriptionDefinition(this);

        if (playerSenderOnlyFlag && consoleSenderOnlyFlag)
            throw new ACLFCriticalException("Command can not be only for player and only for console at the same time. [" + commandName + ":" + subCommandName + "]");
    }

    /**
     * Scans annotation definitions
     * Sets {@link #opsOnlyFlag} and fills {@link #permissionSet}
     */
    private void scanMethodForDefinitions() {
        Class<?> controllerClass = controllerBean.getClass();

        //region OpsOnly setting
        OpsOnly opsOnlyAnnotation = controllerClass.getAnnotation(OpsOnly.class);
        if (opsOnlyAnnotation == null) opsOnlyAnnotation = method.getAnnotation(OpsOnly.class);

        opsOnlyFlag = opsOnlyAnnotation != null;
        //endregion

        //region Permissions set filling
        Permitted permittedClassAnnotation = controllerClass.getAnnotation(Permitted.class);
        Permitted permittedMethodAnnotation = method.getAnnotation(Permitted.class);

        if (permittedClassAnnotation != null) permissionSet.addAll(Arrays.asList(permittedClassAnnotation.value()));
        if (permittedMethodAnnotation != null) permissionSet.addAll(Arrays.asList(permittedMethodAnnotation.value()));
        //endregion
    }

    /**
     * Scans method arguments
     */
    private void scanMethodArguments() {

        int argumentsCounter = 0;

        Parameter[] parameters = method.getParameters();
        int counter = 1;
        for (Parameter parameter : parameters) {
            final boolean isTail = counter >= parameters.length && this.tailArgFlag;
            final boolean parsedFromString = !CommandResponse.class.isAssignableFrom(parameter.getType());

            if (PlayerCommandResponse.class.isAssignableFrom(parameter.getType())) this.playerSenderOnlyFlag = true;
            if (ConsoleCommandResponse.class.isAssignableFrom(parameter.getType())) this.consoleSenderOnlyFlag = true;

            Argument annotation = parameter.getAnnotation(Argument.class);

            ArgumentDefinition argumentDefinition = new ArgumentDefinition(parameter.getType(), parsedFromString, annotation, isTail);

            argumentDefinitionList.add(argumentDefinition);

            counter++;
            if (parsedFromString) argumentsCounter++;
        }

        this.argumentsCount = argumentsCounter;
    }

    //region Getters
    @NotNull
    public CommandDescriptionDefinition getDescriptionDefinition() {
        return commandDescriptionDefinition;
    }

    @NotNull
    public Set<String> getPermissionSet() {
        return ImmutableSet.copyOf(permissionSet);
    }

    public boolean hasPlayerOnlyFlag() {
        return playerSenderOnlyFlag;
    }

    public boolean hasConsoleOnlyFlag() {
        return consoleSenderOnlyFlag;
    }

    public boolean hasOpsOnlyFlag() {
        return opsOnlyFlag;
    }

    public boolean hasChatOnlyFlag() {
        return chatOnlyFlag;
    }

    public boolean hasChannelOnlyFlag() {
        return channelOnlyFlag;
    }

    public boolean hasPlayerSenderOnlyFlag() {
        return playerSenderOnlyFlag;
    }

    public boolean hasConsoleSenderOnlyFlag() {
        return consoleSenderOnlyFlag;
    }

    public boolean hasFlags() {
        return playerSenderOnlyFlag ||
               consoleSenderOnlyFlag ||
               opsOnlyFlag ||
               chatOnlyFlag ||
               channelOnlyFlag;
    }

    public int getArgsCount() {
        return argumentsCount;
    }

    @NotNull
    public List<ArgumentDefinition> getArgumentDefinitionList() {
        return ImmutableList.copyOf(argumentDefinitionList);
    }

    @NotNull
    public List<ArgumentDefinition> getStringParsedArgumentDefinitionList() {
        return ImmutableList.copyOf(
                argumentDefinitionList.stream()
                        .filter(ArgumentDefinition::isParsedFromString)
                        .collect(Collectors.toList())
        );
    }

    @NotNull
    public String getCommandName() {
        return commandName;
    }

    @NotNull
    public String getSubCommandName() {
        return subCommandName;
    }

    @NotNull
    public Object getControllerBean() {
        return controllerBean;
    }

    @NotNull
    public Method getMethod() {
        return method;
    }
    //endregion


    @Override
    public String toString() {
        String fullCommandName = getCommandName() + ":" + getSubCommandName();

        StringBuilder toStringStringBuilder = new StringBuilder();

        toStringStringBuilder
                .append(YELLOW)
                .append(fullCommandName)
                .append(AQUA)
                .append(" from ")
                .append(ChatColor.YELLOW)
                .append(getControllerBean().getClass().getSimpleName())
                .append(".")
                .append(getMethod().getName())
                .append("()");

        if (hasFlags()) {
            toStringStringBuilder
                    .append(ChatColor.AQUA)
                    .append(" [Flags:")

                    .append(ChatColor.RED)
                    .append(hasOpsOnlyFlag() ? " [OpsOnly]" : "")
                    .append(hasPlayerOnlyFlag() ? " [PlayersOnly]" : "")
                    .append(hasConsoleOnlyFlag() ? " [ConsoleOnly]" : "")
                    .append(hasChannelOnlyFlag() ? " [ChannelOnly]" : "")
                    .append(hasChatOnlyFlag() ? " [ChatOnly]" : "")
                    .append(ChatColor.AQUA)
                    .append("]");
        }

        if (getPermissionSet().size() > 0) {
            toStringStringBuilder
                    .append(ChatColor.AQUA)
                    .append(" [Permissions: ")
                    .append(ChatColor.GOLD)
                    .append(Arrays.toString(getPermissionSet().toArray()))
                    .append(ChatColor.AQUA)
                    .append("]");
        }

        return toStringStringBuilder.toString();
    }

}