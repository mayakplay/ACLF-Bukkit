package com.mayakplay.aclf.infrastructure;

import com.mayakplay.aclf.ACLF;
import com.mayakplay.aclf.event.ChannelCommandReceiveEvent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class IncomingPluginMessageListener implements PluginMessageListener {

    public IncomingPluginMessageListener() {
        Bukkit.getMessenger().registerIncomingPluginChannel(ACLF.getACLF(), "ACLF_COMMANDS", this);
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] bytes) {
        try {
            String message = new String(bytes);
            message = message.substring(StringUtils.indexOf(message, ":") + 1);

            System.out.println(" received: " + message);

            Bukkit.getPluginManager().callEvent(new ChannelCommandReceiveEvent(player, message));
        } catch (Exception ignored) {}
    }

}