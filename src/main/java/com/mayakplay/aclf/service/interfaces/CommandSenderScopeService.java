package com.mayakplay.aclf.service.interfaces;

import com.mayakplay.aclf.infrastructure.SenderScopeContext;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 21.06.2019.
 */
public interface CommandSenderScopeService {

    @Nullable
    SenderScopeContext getContextFor(CommandSender sender);

    @NotNull
    String getSpecifySenderName(@NotNull CommandSender sender);

}