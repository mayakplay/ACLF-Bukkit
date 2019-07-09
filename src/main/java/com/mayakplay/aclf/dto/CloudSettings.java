package com.mayakplay.aclf.dto;

import org.jetbrains.annotations.NotNull;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 07.07.2019.
 */
public interface CloudSettings {

    int getNuggetPort();

    @NotNull
    String getServerType();

    int getBungeeNuggetPort();

    @NotNull
    String getBungeeIp();

    @NotNull
    String getBungeePassword();

}
