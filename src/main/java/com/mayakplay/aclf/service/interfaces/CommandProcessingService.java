package com.mayakplay.aclf.service.interfaces;

import com.mayakplay.aclf.annotation.Argument;
import com.mayakplay.aclf.annotation.CommandController;
import com.mayakplay.aclf.pojo.DeprecatedCommandDefinition;
import com.mayakplay.aclf.stereotype.ArgumentParser;
import com.mayakplay.aclf.type.CommandProcessOutput;
import com.mayakplay.aclf.type.SenderType;
import org.bukkit.command.CommandSender;

/**
 * Command processing service.
 * Processed command message arguments.
 *
 * @author mayakplay
 * @version 0.0.1
 * @since 09.06.2019.
 *
 * @see CommandController
 * @see Argument
 * @see ArgumentParser
 * @see CommandRegistryService
 */
public interface CommandProcessingService {

    /**
     * Method invokes command method if message and sender matches the conditions.
     * First word of the message is the {@link DeprecatedCommandDefinition#getCommandName()} and
     * second are {@link DeprecatedCommandDefinition#getSubCommandName()} or empty string &#34;&#34;.
     *
     * Both of it casts to lower case {@link String#toLowerCase()}
     *
     * @param message full command message excluding &#47;
     * @param sender sender of command
     * @param senderType player, player, but from channel and console.
     */
    CommandProcessOutput process(String message, CommandSender sender, SenderType senderType);

}