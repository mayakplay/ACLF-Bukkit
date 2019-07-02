package com.mayakplay.aclf.controller;

import com.mayakplay.aclf.ACLF;
import com.mayakplay.aclf.annotation.*;
import com.mayakplay.aclf.definition.response.CommandResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 26.06.2019.
 */
@CommandController
@CommandMapping("test")
public class AnotherTestController {

    @ChatMapping("test")
    public void something(CommandResponse response) {
        if (response.getSender() instanceof Player) {
            Player player = (Player) response.getSender();

        }
    }

    @ChatMapping("o")
    @OpsOnly
    public void something1() throws InterruptedException {
        synchronized (ACLF.getACLF()) {
            System.out.println(Thread.currentThread());
            System.out.println("OPA4KI");



            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "1234");
        }
    }

    // /test test
    // /test o

}