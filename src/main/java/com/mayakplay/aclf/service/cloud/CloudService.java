package com.mayakplay.aclf.service.cloud;

import com.mayakplay.aclf.exception.UnconnectedGatewayException;
import com.mayakplay.aclf.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 25.07.2019.
 */
public interface CloudService {

    void sendNugget(String message, String tag, Pair<String, String>... parameters) throws UnconnectedGatewayException;

    @Nullable
    String getAssignedId();

    @NotNull
    String getClientType();

}