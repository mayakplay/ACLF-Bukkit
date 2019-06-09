package com.mayakplay.aclf.service.interfaces;

import com.mayakplay.aclf.type.SenderType;
import org.bukkit.command.CommandSender;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 09.06.2019.
 */
public interface CommandProcessingService {

    boolean process(String message, CommandSender sender, SenderType senderType);

}