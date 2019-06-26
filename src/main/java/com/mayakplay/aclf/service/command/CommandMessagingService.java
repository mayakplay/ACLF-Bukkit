package com.mayakplay.aclf.service.command;

import com.mayakplay.aclf.definition.CommandDefinition;
import com.mayakplay.aclf.definition.response.CommandResponse;
import com.mayakplay.aclf.type.ArgumentMistakeType;

import java.util.List;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 25.06.2019.
 */
public interface CommandMessagingService {

    void sendResponseMessage(CommandResponse response, String exceptionMessage);

    String getUsageMessage(CommandDefinition commandDefinition, List<ArgumentMistakeType> argumentMistakeTypes);

}