package com.mayakplay.aclf.util;

import com.google.common.collect.ImmutableMap;
import com.mayakplay.aclf.ACLF;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class StaticUtils {

    private static List<Pair<String, ChatColor>> replaceList = new ArrayList<>();
    static {
        replaceList.add(Pair.of("{BLACK}", ChatColor.BLACK));
        replaceList.add(Pair.of("{DARK_BLUE}", ChatColor.DARK_BLUE));
        replaceList.add(Pair.of("{DARK_GREEN}", ChatColor.DARK_GREEN));
        replaceList.add(Pair.of("{DARK_AQUA}", ChatColor.DARK_AQUA));
        replaceList.add(Pair.of("{DARK_RED}", ChatColor.DARK_RED));
        replaceList.add(Pair.of("{DARK_PURPLE}", ChatColor.DARK_PURPLE));
        replaceList.add(Pair.of("{GOLD}", ChatColor.GOLD));
        replaceList.add(Pair.of("{GRAY}", ChatColor.GRAY));
        replaceList.add(Pair.of("{DARK_GRAY}", ChatColor.DARK_GRAY));
        replaceList.add(Pair.of("{BLUE}", ChatColor.BLUE));
        replaceList.add(Pair.of("{GREEN}", ChatColor.GREEN));
        replaceList.add(Pair.of("{AQUA}", ChatColor.AQUA));
        replaceList.add(Pair.of("{RED}", ChatColor.RED));
        replaceList.add(Pair.of("{LIGHT_PURPLE}", ChatColor.LIGHT_PURPLE));
        replaceList.add(Pair.of("{YELLOW}", ChatColor.YELLOW));
        replaceList.add(Pair.of("{WHITE}", ChatColor.WHITE));
        replaceList.add(Pair.of("{MAGIC}", ChatColor.MAGIC));
        replaceList.add(Pair.of("{BOLD}", ChatColor.BOLD));
        replaceList.add(Pair.of("{STRIKETHROUGH}", ChatColor.STRIKETHROUGH));
        replaceList.add(Pair.of("{UNDERLINE}", ChatColor.UNDERLINE));
        replaceList.add(Pair.of("{ITALIC}", ChatColor.ITALIC));
    }

    public static String replceColors(String stringWithTags) {
        String[] keys = new String[replaceList.size()];
        String[] values = new String[replaceList.size()];

        for (int i = 0; i < replaceList.size(); i++) {
            Pair<String, ChatColor> pair = replaceList.get(i);

            keys[i] = pair.getFirst();
            values[i] = pair.getSecond() + "";
        }


        return StringUtils.replaceEach(stringWithTags, keys, values);
    }

    /**
     * The method converts a map with an inner map into an immutable map.
     *
     * @param map mutable map
     * @param <K> first key
     * @param <T> second key
     * @param <V> value
     *
     * @return immutable version of Map with inner value Map
     */
    public static <K, T, V> Map<K, Map<T, V>> getImmutableMapWithInnerMapInValue(Map<K, Map<T, V>> map) {
        Map<K, ImmutableMap<T, V>> newMap = map.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, stringMapEntry -> ImmutableMap.copyOf(stringMapEntry.getValue()), (a, b) -> b));

        return ImmutableMap.copyOf(newMap);
    }

    /**
     * @param booleans booleans where you need to count trues
     */
    public static int countTrues(boolean... booleans) {
        int counter = 0;
        for (boolean bool : booleans) {
            if (bool) counter++;
        }
        return counter;
    }

    /**
     * Surrounded version of {@link #registerCommand(String, Plugin)}
     * @param name
     */
    public static void registerCommand(String name) {
        try {
            registerCommand(name, ACLF.getACLF());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <a href="https://gist.github.com/nightpool/8129675">Author's git</a>
     */
    private static void registerCommand(String name, Plugin plugin) throws Exception {
        PluginCommand command;

        Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
        c.setAccessible(true);

        command = c.newInstance(name, plugin);

        if (getCommandMap().getCommand(name) == null) {
            getCommandMap().register(plugin.getDescription().getName(), command);
        }
    }


    /**
     * <a href="https://gist.github.com/nightpool/8129675">Author's git</a>
     */
    private static CommandMap getCommandMap() {
        CommandMap commandMap = null;

        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field f = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);

                commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return commandMap;
    }

}