package com.mayakplay.aclf.service.translation;

import com.mayakplay.aclf.ACLF;
import com.mayakplay.aclf.definition.TranslationContainer;
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

    private final HashMap<Plugin, TranslationContainer> translationContainerMap = new HashMap<>();

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

        for (JarEntry entry : collect) {
            System.out.println(entry);

            Yaml yaml = new Yaml();
            Map<String, String> load = yaml.load(classLoader.getResourceAsStream(entry.getName()));

            for (Map.Entry<String, String> loadEntry : load.entrySet()) {
                System.out.println(" - " + loadEntry.getKey() + ":" + loadEntry.getValue());
            }
        }

    }

    @Override
    public String getTranslated(Plugin plugin, String key, Locale locale) {
        return key;
    }

    @Override
    public Map<Plugin, Map<String, String>> getTranslationInfoMap() {
        return null;
    }

    @Override
    public void reload() {

    }

}
