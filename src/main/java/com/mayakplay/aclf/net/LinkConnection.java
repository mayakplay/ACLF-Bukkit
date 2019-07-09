package com.mayakplay.aclf.net;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author winterprison
 * @version 0.0.1
 * @since 06.07.2019.
 */
public class LinkConnection {

    private String address;
    private int port;
    private LinkServer.ServerType type;
    public final Queue<LinkQueue> messageQueue = new ArrayDeque<>();


    public LinkConnection(String address, int port) {
        this.address = address;
        this.port = port;
        this.type = LinkServer.ServerType.UNDEFINED;
    }

    public LinkConnection(String address, int port, LinkServer.ServerType type) {
        this.address = address;
        this.port = port;
        this.type = type;
    }

    public final String getAddress() {
        return address;
    }

    public final int getPort() {
        return port;
    }

    public LinkServer.ServerType getType() {
        return type;
    }
}
