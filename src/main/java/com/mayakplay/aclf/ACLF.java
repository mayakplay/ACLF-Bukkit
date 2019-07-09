package com.mayakplay.aclf;

import com.google.common.collect.ImmutableList;
import com.mayakplay.aclf.event.ContextsCreationCompletionEvent;
import com.mayakplay.aclf.event.TickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * TODO: javadoc
 */
public final class ACLF extends JavaPlugin {

    private byte tickCounter = 0;
    private static AddonDefinitionScanner addonDefinitionContainer;

    private static final Locale serverLocale = Locale.US;

    /**
     * Plugins with ACLF dependency in 'plugin.yml'
     */
    private List<Plugin> dependentPlugins;

    @NotNull
    public synchronized static ACLF getACLF() {
        return JavaPlugin.getPlugin(ACLF.class);
    }

    @Nullable
    public static Plugin getPluginByClass(Class<?> pluginClass) {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin.getClass().getClassLoader().equals(pluginClass.getClassLoader())) {
                return plugin;
            }
        }
        return null;
    }

    @Nullable
    public static AddonDefinitionScanner getAddonDefinitionScanner() {
        return addonDefinitionContainer;
    }

    @Override
    public void onEnable() {
        //Dependent plugins setting
        dependentPlugins =      ImmutableList.copyOf(findDependentPlugins());

        //Counts plugins with ACLF dependency
        long candidatesCount = Arrays.stream(Bukkit.getPluginManager().getPlugins())
                .flatMap(plugin -> plugin.getDescription().getDepend().stream())
                .filter(s -> s.equalsIgnoreCase(this.getName()))
                .count();

        //Start message printing
        printGreetingMessage(
                getDescription().getVersion(),
                candidatesCount,
                serverLocale.getLanguage(),
                getServer().getName()
        );

        //Register addons scanner
        addonDefinitionContainer = new AddonDefinitionScanner();

        //Completion event calling
        Bukkit.getPluginManager().callEvent(new ContextsCreationCompletionEvent());

        //Starts tick emulation.
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::onTick, 1L, 0);

    }

    /**
     * Scans all plugins, looking for dependencies on ACLF.
     * Add dependent plugins to hash map
     *
     * Code from 'plugin.yml'
     *  <code>
     *     depend:
     *      - ACLFramework
     * </code>
     */
    private List<Plugin> findDependentPlugins() {
        return Arrays.stream(Bukkit.getPluginManager().getPlugins())
                .filter(plugin -> plugin.getDescription().getDepend().contains(ACLF.getACLF().getName()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Plugin> getDependentPlugins() {
        return ACLF.getACLF().dependentPlugins;
    }

    public static Locale getServerLocale() {
        return serverLocale;
    }

    /**
     * &#47;reload blocking
     */
    @Override
    public void onDisable() {
        Bukkit.shutdown();
    }

    private void printGreetingMessage(String version, long candidatesCount, String language, String serverName) {
        String message1 = ChatColor.GOLD + "- aclf version: " + ChatColor.YELLOW + version + ChatColor.AQUA;
        String message2 = ChatColor.GOLD + "- addons candidates: " + ChatColor.YELLOW + candidatesCount + ChatColor.AQUA;
        String message3 = ChatColor.GOLD + "- current language: " + ChatColor.YELLOW + language + ChatColor.AQUA;
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