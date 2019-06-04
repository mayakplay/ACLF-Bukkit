package com.mayakplay.aclf.service;

import com.google.common.collect.Lists;
import com.mayakplay.aclf.ACLF;
import com.mayakplay.aclf.exception.ACLFCriticalException;
import com.mayakplay.aclf.service.interfaces.TranslationService;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.mayakplay.aclf.util.ReflectionUtils.getMethodCallerClass;

@Component
public class ACLFTranslationService implements TranslationService, BeanPostProcessor {

    /**
     * PluginName, [Translation name, translation]
     */
    private Map<Plugin, Map<String, String>> translationInfoMap;

    public ACLFTranslationService() {
        try {
            System.out.println("teststetqedfqwef");
            init();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * contains current lang file ->
     * contains default lang file ->
     * contains any lang file ->
     * contains no lang files
     */
    private void init() throws FileNotFoundException {

        LinkedList<Plugin> plugins = new LinkedList<>(ACLF.getDependentPlugins());
        plugins.addFirst(ACLF.getACLF());

        for (Plugin plugin : plugins) {

            System.out.println(plugin.getName());

            File pluginDataFolder = plugin.getDataFolder();
            File pluginLangFolder = new File(pluginDataFolder, "lang");

            if (!pluginLangFolder.mkdirs()) {

                FilenameFilter filenameFilter = (dir, name) -> name.endsWith(".lang");
                File[] files = pluginLangFolder.listFiles(filenameFilter);
                if (files == null) continue;

                ArrayList<File> langFilesList = Lists.newArrayList(files);

                File currentLangFile = getCurrentLangFile(langFilesList, plugin);

            }
        }
    }

    public File getCurrentLangFile(List<File> langFilesList, Plugin plugin) {
        File currentLangFile = langFilesList.stream()
                .filter(file -> file.getName().equals(ACLF.getCurrentLocale().getLanguage() + ".lang"))
                .findFirst()
                .orElse(null);

        if (currentLangFile == null) {
            currentLangFile = langFilesList.stream()
                    .filter(file -> file.getName().equals(ACLF.getDefaultLocale().getLanguage() + ".lang"))
                    .findFirst()
                    .orElse(null);
        }

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
        return getTranslated(ACLF.getPluginByClass(getMethodCallerClass()), key);
    }

    @Override
    public String getTranslated(Plugin plugin, String key) {
        Map<String, String> translationsMap = translationInfoMap.get(plugin);
        return translationsMap != null ? translationsMap.get(key) : null;
    }

    @Override
    public Map<String, Map<String, String>> getTranslationInfoMap() {
        return null;
    }

    @Override
    public void reload() {

    }
}
