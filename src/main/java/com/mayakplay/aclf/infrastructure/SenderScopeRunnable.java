package com.mayakplay.aclf.infrastructure;

/**
 * Same as {@link Runnable} but with throws XD
 *
 * @author mayakplay
 * @version 0.0.1
 * @since 25.06.2019.
 */
@FunctionalInterface
public interface SenderScopeRunnable {

    void run() throws Throwable;

}