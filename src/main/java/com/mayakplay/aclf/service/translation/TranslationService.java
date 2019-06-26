package com.mayakplay.aclf.service.translation;

import com.mayakplay.aclf.ACLF;
import com.mayakplay.aclf.util.ReflectionUtils;
import org.bukkit.plugin.Plugin;

import java.util.Locale;
import java.util.Map;

import static com.mayakplay.aclf.util.ReflectionUtils.getMethodCallerClass;

public interface TranslationService {

    String getTranslated(Plugin plugin, String key, Locale locale);

    Map<Plugin, Map<String, String>> getTranslationInfoMap();

    void reload();

    default String getTranslated(String key) {
        Plugin pluginByClass = ACLF.getPluginByClass(ReflectionUtils.getMethodCallerClass());

        return getTranslated(pluginByClass, key, ACLF.getServerLocale());
    }

    default String getTranslated(Plugin plugin, String key) {
        return getTranslated(plugin, key, ACLF.getServerLocale());
    }

    default String getTranslated(String key, Locale locale) {
        Plugin pluginByClass = ACLF.getPluginByClass(getMethodCallerClass());

        return getTranslated(pluginByClass, key, locale);
    }

}