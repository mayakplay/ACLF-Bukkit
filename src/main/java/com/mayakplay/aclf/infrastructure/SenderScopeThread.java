package com.mayakplay.aclf.infrastructure;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 21.06.2019.
 */
public final class SenderScopeThread extends Thread {

    private LinkedBlockingQueue<Runnable> tasksQueue = new LinkedBlockingQueue<>();

    public SenderScopeThread(@NotNull String name) {
        super(name);
    }

    @SneakyThrows
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while (true) {
            if (tasksQueue.peek() != null) {
                Runnable task = tasksQueue.poll();

                task.run();
            }
        }
    }

    public void handleCallback(Runnable runnable) {
        tasksQueue.add(runnable);
    }

}