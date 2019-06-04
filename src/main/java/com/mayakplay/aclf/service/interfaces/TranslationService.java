package com.mayakplay.aclf.service.interfaces;

import org.bukkit.plugin.Plugin;

import java.util.Map;

public interface TranslationService {

    String getTranslated(String key);

    String getTranslated(Plugin plugin, String key);

    Map<Plugin, Map<String, String>> getTranslationInfoMap();

    void reload();

}