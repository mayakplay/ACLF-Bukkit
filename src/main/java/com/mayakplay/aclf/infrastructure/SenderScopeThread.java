package com.mayakplay.aclf.infrastructure;

import com.mayakplay.aclf.service.interfaces.CommandRegistryService;
import lombok.AllArgsConstructor;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 21.06.2019.
 */
@AllArgsConstructor
public final class SenderScopeThread extends Thread {

    private CommandRegistryService commandRegistryService;

    public void run() {

    }

}
