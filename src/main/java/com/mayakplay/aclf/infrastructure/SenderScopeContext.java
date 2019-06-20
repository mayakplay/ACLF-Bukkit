package com.mayakplay.aclf.infrastructure;

import com.mayakplay.aclf.ACLF;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 21.06.2019.
 */
@Getter
public final class SenderScopeContext {

    private CommandSender sender;
    private SenderScopeThread senderScopeThread;
    private Integer timerId;

    public SenderScopeContext(CommandSender sender, SenderScopeThread senderScopeThread) {
        this.sender = sender;
        this.senderScopeThread = senderScopeThread;

        senderScopeThread.start();
        System.out.println("!!!Thread for [" + sender.getName() + "] stared");
    }

    public void startContextRemoveTimer(long removeTimeTicks, Runnable callback) {
        if (timerId != null) return;

        System.out.println("!!!Player [" + sender.getName() + "] has " + removeTimeTicks + " ticks to reconnect");
        Runnable runnable = () -> {
            System.out.println("!!!Thread killed for [" + sender.getName() + "]");
            timerId = null;
            callback.run();
        };

        timerId = Bukkit.getScheduler().scheduleSyncDelayedTask(ACLF.getACLF(), runnable, removeTimeTicks);
    }

    public void stopRemoveTimer() {
        BukkitScheduler bukkitScheduler = Bukkit.getScheduler();

        if (timerId != null) {
            System.out.println("!!!Player [" + sender.getName() + "] join before timer execution. Timer closed");
            bukkitScheduler.cancelTask(timerId);
            timerId = null;
        }

    }

}
