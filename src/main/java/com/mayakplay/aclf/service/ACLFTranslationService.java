package com.mayakplay.aclf.service;

import com.mayakplay.aclf.ACLF;
import com.mayakplay.aclf.service.interfaces.TranslationService;
import org.bukkit.plugin.Plugin;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

import static com.mayakplay.aclf.util.ReflectionUtils.getMethodCallerClass;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 10.06.2019.
 */
@Component
public class ACLFTranslationService implements TranslationService {

    @Override
    public String getTranslated(String key) {
        Plugin pluginByClass = ACLF.getPluginByClass(getMethodCallerClass());

        return getTranslated(pluginByClass, key, ACLF.getServerLocale());
    }

    @Override
    public String getTranslated(Plugin plugin, String key) {

        return getTranslated(plugin, key, ACLF.getServerLocale());
    }

    @Override
    public String getTranslated(String key, Locale locale) {
        Plugin pluginByClass = ACLF.getPluginByClass(getMethodCallerClass());

        return getTranslated(pluginByClass, key, locale);
    }

    @Override
    public String getTranslated(Plugin plugin, String key, Locale locale) {
        return "::" + plugin.getName() + "::";
    }

    @Override
    public Map<Plugin, Map<String, String>> getTranslationInfoMap() {
        return null;
    }

    @Override
    public void reload() {

    }

}
