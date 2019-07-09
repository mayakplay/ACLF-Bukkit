package com.mayakplay.aclf.service.cloud;

import com.mayakplay.aclf.infrastructure.Nugget;
import org.jetbrains.annotations.NotNull;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 05.07.2019.
 */
public interface NuggetService {

    void sendToBungee(@NotNull Nugget nugget);

}