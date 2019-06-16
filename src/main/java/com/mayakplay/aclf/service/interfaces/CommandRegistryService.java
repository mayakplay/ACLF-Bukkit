package com.mayakplay.aclf.service.interfaces;

import com.mayakplay.aclf.pojo.DeprecatedCommandDefinition;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 09.06.2019.
 */
public interface CommandRegistryService {

    boolean containsCommand(String command, String subCommand);

    DeprecatedCommandDefinition getCommandDefinition(String command, String subCommand);

    DeprecatedCommandDefinition getCommandDefinitionByMessage(String message);

}
