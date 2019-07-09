package com.mayakplay.aclf.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 07.07.2019.
 */
@AllArgsConstructor
@Getter
@ToString
public class ACLFCloudSettings implements CloudSettings {

    private final int nuggetPort;
    private final String serverType;
    private final int bungeeNuggetPort;
    private final String bungeeIp;
    private final String bungeePassword;

}
