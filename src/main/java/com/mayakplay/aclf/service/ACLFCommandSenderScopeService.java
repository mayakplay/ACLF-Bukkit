package com.mayakplay.aclf.service;

import com.mayakplay.aclf.infrastructure.SenderScopeContext;
import com.mayakplay.aclf.infrastructure.SenderScopeThread;
import com.mayakplay.aclf.service.interfaces.CommandRegistryService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 21.06.2019.
 */
@Service
public class ACLFCommandSenderScopeService implements Listener {

    private static final long THREAD_REMOVE_TIME_TICKS = 10;
    private final HashMap<String, SenderScopeContext> sendersMap = new HashMap<>();

    private CommandRegistryService commandRegistryService;

    public ACLFCommandSenderScopeService(CommandRegistryService commandRegistryService) {
        this.commandRegistryService = commandRegistryService;

        final ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();

        final SenderScopeThread consoleSenderScopeThread = new SenderScopeThread(commandRegistryService);
        final SenderScopeContext consoleSenderScopeContext = new SenderScopeContext(consoleCommandSender, consoleSenderScopeThread);

        sendersMap.put(getSpecifySenderName(consoleCommandSender), consoleSenderScopeContext);
    }

    //region event
    @EventHandler
    private void handleJoinEvent(PlayerJoinEvent event) {
        final Player playerSender = event.getPlayer();
        final String senderName = getSpecifySenderName(playerSender);

        SenderScopeContext senderScopeContext = sendersMap.computeIfAbsent(senderName, s -> createSenderContext(playerSender));
        senderScopeContext.stopRemoveTimer();
    }

    @EventHandler
    private void handleLeaveEvent(PlayerQuitEvent event) {
        final Player playerSender = event.getPlayer();
        final String senderName = getSpecifySenderName(playerSender);

        SenderScopeContext playerSenderScopeContext = sendersMap.get(getSpecifySenderName(playerSender));
        playerSenderScopeContext.startContextRemoveTimer(THREAD_REMOVE_TIME_TICKS, () -> sendersMap.remove(senderName));
    }
    //endregion

    //region Util
    @NotNull
    private SenderScopeContext createSenderContext(CommandSender sender) {
        SenderScopeThread senderScopeThread = new SenderScopeThread(commandRegistryService);
        return new SenderScopeContext(sender, senderScopeThread);
    }

    @NotNull
    public String getSpecifySenderName(@NotNull CommandSender sender) {
        Objects.requireNonNull(sender);
        return sender.getClass().getSimpleName() + ":" + sender.getName();
    }
    //endregion

}