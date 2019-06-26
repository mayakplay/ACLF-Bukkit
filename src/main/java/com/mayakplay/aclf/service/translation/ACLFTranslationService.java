package com.mayakplay.aclf.service.translation;

import com.mayakplay.aclf.ACLF;
import com.mayakplay.aclf.definition.TranslationContainer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

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
        @SuppressWarnings({"NonAsciiCharacters", "ConstantConditions"})
        File тыМнеТакПомогДруг = new File(plugin.getClass().getClassLoader().getResource("plugin.yml").getFile());
        String parent = тыМнеТакПомогДруг.getParent();
        File file = new File(parent.substring(6, parent.length() - 1));

        JarFile jarFile = new JarFile(file);

        List<JarEntry> collect = jarFile.stream()
                .filter(jarEntry ->
                        jarEntry.getName().startsWith(LANG_FOLDER_NAME) && jarEntry.getName().endsWith(".lang"))
                .collect(Collectors.toList());

        for (JarEntry entry : collect) {
            System.out.println(entry);
        }

//        for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
//            JarEntry jarEntry = entries.nextElement();
//
//
//            System.out.println(jarEntry.getName());
//        }


//        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(this.getClass().getClassLoader());
//
//        for (Resource resource : resolver.getResources("/language/")) {
//            System.out.println(resource.getFilename());
//        }

        //--------------

//        try (InputStream inputStream = plugin.getResource("language/en.lang")) {
//
//            assert inputStream != null;
//
//            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
//
//            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                System.out.println(line);
//            }
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }


//        String filename = "language";
//
//        try {
//            URL url = plugin.getClass().getClassLoader().getResource(filename);
//
//            if (url == null) {
//                System.out.println("hueta, a ne fael");
//            }
//
//            URLConnection connection = url.openConnection();
//            connection.setUseCaches(false);
//
//            File file = new File(connection.getURL().toURI());
//            System.out.println(file.exists());
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }


//        File file = new File(Objects.requireNonNull(classLoader.getResources("\\")).getFile());
//
//        System.out.println(file.isDirectory());
//
//        FilenameFilter filenameFilter = (dir, name) -> name.endsWith(".lang");
//
//        System.out.println(file.exists());
//        System.out.println(file.getAbsolutePath());
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
