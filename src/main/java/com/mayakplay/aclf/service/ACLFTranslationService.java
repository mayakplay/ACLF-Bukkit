package com.mayakplay.aclf.service;

import com.google.common.collect.ImmutableMap;
import com.mayakplay.aclf.service.interfaces.TranslationService;
import org.bukkit.plugin.Plugin;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.Map;

@Service
public class ACLFTranslationService implements TranslationService {

    /**
     * PluginName, [Translation name, translation]
     */
    private Map<String, Map<String, String>> translationInfoMap;

    public ACLFTranslationService() {
        try {
            init();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void init() throws FileNotFoundException {
//        translationInfoMap = new HashMap<>();
//        final List<Plugin> plugins = new ArrayList<>(ACLF.getPlugins());
//        plugins.add(ACLF.getACLF());
//
//        for (Plugin plugin : plugins) {
//            File dataFolder = plugin.getDataFolder();
//
//            File pluginLangFolder = new File(dataFolder, "lang");
//
//            if (!pluginLangFolder.mkdirs()) {
//                FilenameFilter filter = (directory, fileName) -> fileName.endsWith(".lang");
//
//                File[] files = pluginLangFolder.listFiles(filter);
//
//                final Map<String, String> stringMap = new HashMap<>();
//
//                if (files != null) {
//                    for (File file : files) {
//                        Yaml yaml = new Yaml();
//
//                        InputStream inputStream = new FileInputStream(file);
//                        Map<String, String> load = yaml.load(inputStream);
//                        stringMap.putAll(load);
//
//                    }
//                }
//
//                translationInfoMap.put(plugin.getName(), stringMap);
//            }
//        }
    }

    @Override
    public String getTranslated(Plugin plugin, String string) {
        return getTranslated(plugin.getName(), string);
    }

    @Override
    public String getTranslated(String pluginName, String string) {
        return !translationInfoMap.containsKey(pluginName) ? string : translationInfoMap.get(pluginName).getOrDefault(string, string);
    }

    @Override
    public Map<String, Map<String, String>> getTranslationInfoMap() {
        return ImmutableMap.copyOf(translationInfoMap);
    }

    @Override
    public void reload() {
        try {
            init();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
