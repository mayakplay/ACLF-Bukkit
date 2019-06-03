package com.mayakplay.aclf;

import com.mayakplay.aclf.event.TickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

/**
 * TODO: javadoc
 */
public final class ACLFPluginAdapter extends JavaPlugin {

    private byte tickCounter = 0;
    private static AddonDefinitionScanner addonDefinitionContainer;

    @Override
    public void onEnable() {
        //Counts plugins with ACLF dependency
        long candidatesCount = Arrays.stream(Bukkit.getPluginManager().getPlugins())
                .flatMap(plugin -> plugin.getDescription().getDepend().stream())
                .filter(s -> s.equalsIgnoreCase(this.getName()))
                .count();

        printGreetingMessage(getDescription().getVersion(), candidatesCount, getServer().getIp() + ":" + getServer().getPort(), getServer().getName());

        //Register addons scanner
        addonDefinitionContainer = new AddonDefinitionScanner();

        //Starts tick emulation.
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::onTick, 1L, 0);
    }

    /**
     * &#47;reload blocking
     */
    @Override
    public void onDisable() {
        Bukkit.shutdown();
    }

    private void printGreetingMessage(String version, long candidatesCount, String serverUrl, String serverName) {
        String message1 = ChatColor.GOLD + "- aclf version: " + ChatColor.YELLOW + version + ChatColor.AQUA;
        String message2 = ChatColor.GOLD + "- addons candidates: " + ChatColor.YELLOW + candidatesCount + ChatColor.AQUA;
        String message3 = ChatColor.GOLD + "- server url: " + ChatColor.YELLOW + serverUrl + ChatColor.AQUA;
        String message4 = ChatColor.GOLD + "- server name: " + ChatColor.YELLOW + serverName + ChatColor.AQUA;

        Bukkit.getConsoleSender().sendMessage(
                ChatColor.AQUA + "\n" +
                "========================================================================================================================\n" +
                "      _    ____ _     _____   | "    + ChatColor.YELLOW + "Enabling..." + ChatColor.AQUA + "\n" +
                "     / \\  / ___| |   |  ___|  |  "  + message1 + ChatColor.AQUA + "\n" +
                "    / _ \\| |   | |   | |_     |  "  + message2 + ChatColor.AQUA + "\n" +
                "   / ___ \\ |___| |___|  _|    |  "  + message3 + ChatColor.AQUA + "\n" +
                "  /_/   \\_\\____|_____|_|      |  " + message4 + ChatColor.AQUA + "\n" +
                "========================================================================================================================");
    }

    //region tick emulation processing
    /**
     * Invokes on every server tick
     * Depends on TPS (ticks per second)
     *
     * @see TickEvent
     */
    private void onTick() {
        Bukkit.getPluginManager().callEvent(new TickEvent(tickCounter));

        tickCounter++;
        if (tickCounter >= 20) tickCounter = 0;
    }
    //endregion

}