package com.mayakplay.aclf.service;

import com.mayakplay.aclf.event.ControllersClassesScanFinishedEvent;
import com.mayakplay.aclf.infrastructure.SenderScopeContext;
import com.mayakplay.aclf.infrastructure.SenderScopeThread;
import com.mayakplay.aclf.service.interfaces.CommandSenderScopeService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 21.06.2019.
 */
@Service
public class ACLFCommandSenderScopeService implements Listener, CommandSenderScopeService, ApplicationListener<ControllersClassesScanFinishedEvent> {

    private static final long THREAD_REMOVE_TIME_TICKS = 20 * 5; //test
    private final HashMap<String, SenderScopeContext> sendersMap = new HashMap<>();


    @Override
    public void onApplicationEvent(@NotNull ControllersClassesScanFinishedEvent event) {
        final ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();
        final String senderName = getSpecifySenderName(consoleCommandSender);
        final SenderScopeContext senderContext = createSenderContext(consoleCommandSender);

        sendersMap.put(senderName, senderContext);
    }

    @Nullable
    @Override
    public SenderScopeContext getContextFor(CommandSender sender) {
        return sendersMap.get(getSpecifySenderName(sender));
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
        String senderName = getSpecifySenderName(sender);
        SenderScopeThread senderScopeThread = new SenderScopeThread(senderName + "_thread", sender);
        return new SenderScopeContext(sender, senderScopeThread);
    }

    @NotNull
    @Override
    public String getSpecifySenderName(@NotNull CommandSender sender) {
        Objects.requireNonNull(sender);
        return sender.getClass().getSimpleName() + ":" + sender.getName();
    }
    //endregion

}