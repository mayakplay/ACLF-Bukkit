package com.mayakplay.aclf.infrastructure;

import com.mayakplay.aclf.exception.ACLFCommandException;
import com.mayakplay.aclf.exception.ACLFException;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 21.06.2019.
 */
public final class SenderScopeThread extends Thread {

    private final LinkedBlockingQueue<SenderScopeRunnable> asyncTaskQueue = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<SenderScopeCallback> callbackTaskQueue = new LinkedBlockingQueue<>();

    private CommandSender commandSender;

    private Object outputObject = null;

    public SenderScopeThread(@NotNull String name, CommandSender commandSender) {
        super(name);
        this.commandSender = commandSender;
    }

    @SneakyThrows
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while (true) {
            if (callbackTaskQueue.peek() != null) {
                SenderScopeCallback task = callbackTaskQueue.poll();
                outputObject = task.run();
            }

            if (asyncTaskQueue.peek() != null) {
                SenderScopeRunnable task = asyncTaskQueue.poll();
                try {
                    task.run();
                } catch (Throwable throwable) {
                    commandSender.sendMessage(ChatColor.RED + getDeepACLFException(throwable));
                }
            }
        }
    }

    public static String getDeepACLFException(Throwable throwable) {
        Throwable t = recursive(throwable, 0);
        if (t != null)
            return t.getMessage();

        Bukkit.getConsoleSender().sendMessage(
                ChatColor.YELLOW + "@ Please, use " + ChatColor.AQUA + "ACLFCommandException" + ChatColor.YELLOW + " to mark mistakes in command!!!");

        throwable.printStackTrace();
        return "Something goes wrong.";
    }

    private static Throwable recursive(Throwable exception, int counter) {
        if (counter > 10) return null;
        if (exception instanceof ACLFException || exception instanceof ACLFCommandException) {
            return exception;
        } else {
            return exception.getCause() != null ? recursive(exception.getCause(), ++counter) : null;
        }
    }

    @SneakyThrows
    public Object handleCallback(SenderScopeCallback runnable) {
        callbackTaskQueue.add(runnable);

        while (outputObject == null) {
            Thread.sleep(1);
        }

        Object object = outputObject;
        outputObject = null;
        return object;
    }

    public void handleRunnable(SenderScopeRunnable runnable) {
        synchronized (asyncTaskQueue) {
            asyncTaskQueue.add(runnable);
        }
    }

}