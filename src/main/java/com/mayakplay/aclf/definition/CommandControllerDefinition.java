package com.mayakplay.aclf.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 15.06.2019.
 */
@Getter
@AllArgsConstructor
public class CommandControllerDefinition {

    @NotNull
    private final String subCommandName;

    @NotNull
    private final Set<String> permissionSet;

    @NotNull
    private final Class<?> controllerClass;

}