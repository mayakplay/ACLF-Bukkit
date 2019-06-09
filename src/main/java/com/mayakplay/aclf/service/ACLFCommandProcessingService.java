package com.mayakplay.aclf.service;

import com.mayakplay.aclf.event.ChannelCommandReceiveEvent;
import com.mayakplay.aclf.service.interfaces.CommandProcessingService;
import com.mayakplay.aclf.type.SenderType;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 09.06.2019.
 */
@Service
@AllArgsConstructor
public class ACLFCommandProcessingService implements Listener, CommandProcessingService {

    private final ApplicationContext applicationContext;

    @Override
    public boolean process(String message, CommandSender sender, SenderType senderType) {
        return false;
    }

    //region Event handling
    /**
     * Calls {@link #process} method and cancels {@link PlayerCommandPreprocessEvent}
     * if the entered command exists
     *
     * Invokes when player sends command via chat
     * {@link SenderType#PLAYER_CHAT} wrapping
     */
    @EventHandler
    private void playerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        boolean isStarted = process(event.getMessage().substring(1), event.getPlayer(), SenderType.PLAYER_CHAT);

        if (isStarted) event.setCancelled(true);
    }

    /**
     * Calls {@link #process} method and replaces the command
     * with a dummy if the entered command exists
     *
     * Invokes when console command detected
     * {@link SenderType#CONSOLE} wrapping
     */
    @EventHandler
    private void serverCommandEvent(ServerCommandEvent event) {
        boolean isStarted = process(event.getCommand(), event.getSender(), SenderType.CONSOLE);

        if (isStarted) event.setCommand("aclf");
    }

    /**
     * Calls {@link #process} method if the entered command exists
     *
     * Invokes when player sends command via command messaging channel
     * {@link SenderType#PLAYER_CHANNEL} wrapping
     *
     * @see ACLFCommandIncomingPluginMessageListener
     */
    @EventHandler
    private void channelCommandReceiveEvent(ChannelCommandReceiveEvent event) {
        process(event.getCommand(), event.getSender(), SenderType.PLAYER_CHANNEL);
    }
    //endregion

}
