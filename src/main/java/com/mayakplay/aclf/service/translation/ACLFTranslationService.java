package com.mayakplay.aclf.service.translation;

import com.mayakplay.aclf.ACLF;
import com.mayakplay.aclf.definition.AddonTranslationContainer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 10.06.2019.
 */
@Component
public class ACLFTranslationService implements TranslationService {

    private final String LANG_FOLDER_NAME = "language";

    @NotNull private AddonTranslationContainer defaultContainer;
    private final HashMap<Plugin, AddonTranslationContainer> translationContainerMap = new HashMap<>();

    public ACLFTranslationService() {
        final LinkedList<Plugin> plugins = new LinkedList<>(ACLF.getDependentPlugins());
        plugins.addFirst(ACLF.getACLF());

        for (Plugin plugin : plugins) {
            try {
                System.out.println("[start]  ========================================");
                loadLanguageFiles(plugin);
                System.out.println("[finish] ========================================");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.gc();
    }

    private void loadLanguageFiles(@NotNull Plugin plugin) throws Exception {
        ClassLoader classLoader = plugin.getClass().getClassLoader();

        @SuppressWarnings("ConstantConditions")
        File pluginResourceFile = new File(classLoader.getResource("plugin.yml").getFile());
        String parent = pluginResourceFile.getParent();
        File file = new File(parent.substring(6, parent.length() - 1));

        JarFile jarFile = new JarFile(file);

        List<JarEntry> collect = jarFile.stream()
                .filter(jarEntry ->
                        jarEntry.getName().startsWith(LANG_FOLDER_NAME) && jarEntry.getName().endsWith(".lang"))
                .collect(Collectors.toList());

        final HashMap<String, HashMap<String, String>> map = new HashMap<>();
        for (JarEntry entry : collect) {
            String jarFilePath = entry.getName();

            Yaml yaml = new Yaml();
            HashMap<String, String> load = yaml.load(classLoader.getResourceAsStream(jarFilePath));

            String fileName = getFileName(jarFilePath);

            map.put(fileName, load);
        }

        AddonTranslationContainer addonTranslationContainer = AddonTranslationContainer.of(plugin, map);
        translationContainerMap.put(plugin, addonTranslationContainer);

        if (plugin.equals(ACLF.getACLF())) defaultContainer = addonTranslationContainer;
    }

    private String getFileName(String resourcePath) {
        int indexOfSlash = StringUtils.lastIndexOf(resourcePath, '/');

        return resourcePath.substring(indexOfSlash + 1, resourcePath.length() - 5);
    }

    @NotNull
    @Override
    public String getTranslated(Plugin plugin, String key, Locale locale) {
        AddonTranslationContainer addonTranslationContainer = translationContainerMap.get(plugin);
        if (addonTranslationContainer == null) return key;

        String translation = addonTranslationContainer.getTranslation(key, locale.getLanguage());

        if (translation != null) {
            return translation;
        }

        String defaultTranslation = defaultContainer.getTranslation(key, locale.getLanguage());

        return defaultTranslation != null ? defaultTranslation : key;
    }

    @Override
    public void reload() {
        System.out.println("Could not reload");
    }

}
