package com.mayakplay.aclf.service;

import com.google.common.collect.Lists;
import com.mayakplay.aclf.ACLF;
import com.mayakplay.aclf.exception.ACLFCriticalException;
import com.mayakplay.aclf.service.interfaces.TranslationService;
import com.mayakplay.aclf.util.StaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

import static com.mayakplay.aclf.util.ReflectionUtils.getMethodCallerClass;

@Deprecated
public class DeprecatedACLFTranslationService implements TranslationService, BeanPostProcessor {

    /**
     * Plugin, [Translation name, translation]
     */
    private final Map<Plugin, Map<String, String>> translationInfoMap;

    public DeprecatedACLFTranslationService() {
        translationInfoMap = new HashMap<>();

        try {
            System.out.println("teststetqedfqwef");
            init();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void init() throws FileNotFoundException {
        LinkedList<Plugin> plugins = new LinkedList<>(ACLF.getDependentPlugins());
        plugins.addFirst(ACLF.getACLF());

        for (Plugin plugin : plugins) {

            File pluginDataFolder = plugin.getDataFolder();
            File pluginLangFolder = new File(pluginDataFolder, "lang");

            if (!pluginLangFolder.mkdirs()) {

                FilenameFilter filenameFilter = (dir, name) -> name.endsWith(".lang");
                File[] files = pluginLangFolder.listFiles(filenameFilter);
                if (files == null) continue;

                ArrayList<File> langFilesList = Lists.newArrayList(files);

                File currentLangFile = getCurrentLangFile(langFilesList, plugin);

                Yaml yaml = new Yaml();

                InputStream inputStream = new FileInputStream(currentLangFile);
                Map<String, String> load = yaml.load(inputStream);

                if (load != null)
                    translationInfoMap.put(plugin, new HashMap<>(load));
            }
        }

        for (Map.Entry<Plugin, Map<String, String>> entry : translationInfoMap.entrySet()) {
            System.out.println("Translations for: " + entry.getKey().getName());

            for (Map.Entry<String, String> stringEntry : entry.getValue().entrySet()) {
                System.out.println(" - " + stringEntry.getKey() + " = " + stringEntry.getValue());
            }
        }
    }

    public File getCurrentLangFile(List<File> langFilesList, Plugin plugin) {
        File currentLangFile = langFilesList.stream()
                .filter(file -> file.getName().equals(ACLF.getServerLocale().getLanguage() + ".lang"))
                .findFirst()
                .orElse(null);

        if (currentLangFile == null) {
            currentLangFile = langFilesList.stream().findFirst().orElse(null);
        }

        if (currentLangFile == null) {
            throw new ACLFCriticalException("Unnable to find lang file for " + ChatColor.AQUA + plugin.getName());
        }

        return currentLangFile;
    }

    @Override
    public String getTranslated(String key) {
        Plugin pluginByClass = ACLF.getPluginByClass(getMethodCallerClass());
        return getTranslated(pluginByClass, key);
    }

    @Override
    public String getTranslated(Plugin plugin, String key) {
        Map<String, String> translationsMap = translationInfoMap.get(plugin);
        return translationsMap != null ? translationsMap.get(key) : null;
    }

    @Override
    public String getTranslated(String key, Locale locale) {
        return null;
    }

    @Override
    public String getTranslated(Plugin plugin, String key, Locale locale) {
        return null;
    }

    @Override
    public Map<Plugin, Map<String, String>> getTranslationInfoMap() {
        return StaticUtils.getImmutableMapWithInnerMapInValue(translationInfoMap);
    }

    @Override
    public void reload() {
        translationInfoMap.clear();
        try {
            init();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
