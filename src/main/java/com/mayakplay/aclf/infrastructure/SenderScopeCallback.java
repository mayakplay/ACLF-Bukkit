package com.mayakplay.aclf.infrastructure;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 03.07.2019.
 */
@FunctionalInterface
public interface SenderScopeCallback {

    Object run() throws Exception;

}
