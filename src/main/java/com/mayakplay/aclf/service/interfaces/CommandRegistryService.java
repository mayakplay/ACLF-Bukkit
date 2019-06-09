package com.mayakplay.aclf.service.interfaces;

import com.mayakplay.aclf.pojo.CommandDefinition;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 09.06.2019.
 */
public interface CommandRegistryService {

    boolean containsCommand(String command, String subCommand);

    CommandDefinition getCommandDefinition(String command, String subCommand);

}
