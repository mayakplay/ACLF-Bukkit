package com.mayakplay.aclf.service.interfaces;

import org.bukkit.plugin.Plugin;

import java.util.Locale;
import java.util.Map;

public interface TranslationService {

    String getTranslated(String key);

    String getTranslated(Plugin plugin, String key);

    String getTranslated(String key, Locale locale);

    String getTranslated(Plugin plugin, String key, Locale locale);

    Map<Plugin, Map<String, String>> getTranslationInfoMap();

    void reload();

}