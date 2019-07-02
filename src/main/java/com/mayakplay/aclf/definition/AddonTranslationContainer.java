package com.mayakplay.aclf.definition;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 26.06.2019.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddonTranslationContainer {

    @NotNull
    private Plugin plugin;

    @NotNull
    private HashMap<Locale, LocaleContainer> containerHashMap;

    @NotNull
    private HashMap<String, LocaleContainer> langAssociationHashMap;


    public static AddonTranslationContainer of(@NotNull Plugin plugin, @NotNull HashMap<String, HashMap<String, String>> localeTranslationsHashMap) {
        final HashMap<Locale, LocaleContainer> map = new HashMap<>();
        final HashMap<String, LocaleContainer> associationMap = new HashMap<>();


        for (Map.Entry<String, HashMap<String, String>> entry : localeTranslationsHashMap.entrySet()) {
            String localeKey = entry.getKey();
            HashMap<String, String> keyTranslationMap = entry.getValue();

            Locale locale = LocaleUtils.toLocale(localeKey);
            LocaleContainer localeContainer = new LocaleContainer(locale, keyTranslationMap);

            map.put(locale, localeContainer);
            associationMap.put(locale.getLanguage(), localeContainer);
        }

        //region DBG
        System.out.println("[" + plugin.getName() + "] languages:");
        for (Map.Entry<Locale, LocaleContainer> entry : map.entrySet()) {

            Locale locale = entry.getKey();
            LocaleContainer localeContainer = entry.getValue();
            System.out.println(" - " + WordUtils.capitalizeFully(locale.getDisplayName(locale)) + " (" + locale.getLanguage() + "). Detected " + localeContainer.getTranslationsCount() + " translations.");
        }
        //endregion

        return new AddonTranslationContainer(plugin, map, associationMap);
    }

    @Nullable
    public String getTranslation(String key, String locale) {
        LocaleContainer localeContainer = langAssociationHashMap.get(locale);
        if (localeContainer == null) return null;

        return localeContainer.getByKey(key);
    }

    public static void main(String[] args) {
        Locale en = LocaleUtils.toLocale("ru_RU");
        System.out.println(en.getLanguage());

        Locale en1 = LocaleUtils.toLocale("ru_RU");

        System.out.println(en.getDisplayCountry());
        System.out.println(en.getDisplayName());
        System.out.println(en.getLanguage());
        System.out.println(en.getCountry());

        System.out.println(en.getLanguage().equals(en1.getLanguage()));
    }

    //    public static void main(String[] args) {
//
//        String str = "WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
//
//        String wrap = WordUtils.wrap(str, 100);
//
//        for (String s : wrap.split("\n")) {
//            System.out.println(s);
//        }
//
//    }

}