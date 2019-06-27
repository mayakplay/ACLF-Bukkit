package com.mayakplay.aclf.definition;

import org.apache.commons.lang.WordUtils;
import org.bukkit.plugin.Plugin;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 26.06.2019.
 */
public class TranslationContainer {

    private Plugin plugin;

    //TODO: REMOVE
    public static void main(String[] args) {

        String str = "WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

        String wrap = WordUtils.wrap(str, 100);

        for (String s : wrap.split("\n")) {
            System.out.println(s);
        }

    }

}