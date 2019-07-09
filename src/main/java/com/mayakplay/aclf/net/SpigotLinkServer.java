package com.mayakplay.aclf.net;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;

/**
 * @author winterprison
 * @version 0.0.1
 * @since 06.07.2019.
 */
public class SpigotLinkServer extends LinkServer {

    private LinkConnection bungeeServer;

    public SpigotLinkServer(int receivePort) {
        super(receivePort);
    }

    public void registerOnBungee(String bungeeAddress, int bungeePort, String key){
        bungeeServer = new LinkConnection(bungeeAddress,bungeePort,ServerType.BUNGEE);
        addServer("bungee", bungeeServer);
        registerOnBungee(key);
    }

    public void registerOnBungee(String key){
        JsonObject data = new JsonObject();
        data.addProperty("type", ServerType.SPIGOT.toString());
        data.addProperty("port", getReceivePort());
        data.addProperty("key", key);
        data.addProperty("spigot-port", Bukkit.getServer().getPort());
        sendData("bungee", data, jsonObject -> {
            if(jsonObject.has("new_name")){
                String name = jsonObject.get("new_name").getAsString();
                System.out.println("Registered as " + name + ". Starting listening...");
                setServerName(name);
                startListening();
            }
        });
    }
}
