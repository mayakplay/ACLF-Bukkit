package com.mayakplay.aclf.service.interfaces;

import org.bukkit.plugin.Plugin;

import java.util.Map;

public interface TranslationService {

    String getTranslated(Plugin plugin, String string);

    String getTranslated(String pluginName, String string);

    Map<String, Map<String, String>> getTranslationInfoMap();

    void reload();

}