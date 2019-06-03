package com.mayakplay.aclf;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mayakplay.aclf.annotation.ACLFConfiguration;
import com.mayakplay.aclf.exception.ACLFCriticalException;
import com.mayakplay.aclf.util.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mayakplay
 * @version 0.1.1
 * @since 24.05.2019.
 */
public class AddonDefinitionScanner {

    /**
     * Definition associations hash map
     * &lt;Plugin classLoader, Addon definition&gt;
     */
    private final Map<ClassLoader, AddonDefinition> addonDefinitionHashMap;

    /**
     * Plugin name associations map
     * &lt;Plugin classloader, Plugin name&gt;
     */
    private final Map<ClassLoader, Plugin> pluginHashMap;

    /**
     * Plugins with ACLF dependency in 'plugin.yml'
     */
    private final List<Plugin> dependentPlugins;

    /**
     * ACLF spring mainContext.
     */
    private AnnotationConfigApplicationContext mainContext;

    AddonDefinitionScanner() {
        dependentPlugins =      ImmutableList.copyOf(findDependentPlugins());
        pluginHashMap =          ImmutableMap.copyOf(scanForClassLoaders());
        addonDefinitionHashMap = ImmutableMap.copyOf(getAddonDefinitionMap());

        mainContext = new AnnotationConfigApplicationContext();
        //endregion

        //region WARNs If dependent plugin does not contains configuration
        for (Map.Entry<ClassLoader, Plugin> entry : pluginHashMap.entrySet()) {
            if (!addonDefinitionHashMap.containsKey(entry.getKey())) {
                String warningMessage = ChatColor.YELLOW + "\n@WARNING: " + ChatColor.WHITE + "dependent plugin \"" +
                        ChatColor.GOLD + entry.getValue().getName() + ChatColor.WHITE + "\" does not contains ACLFConfiguration!";
                Bukkit.getConsoleSender().sendMessage(warningMessage);
            }
        }
        //endregion

        try {
            registerContext();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void registerContext() throws ClassNotFoundException {

        mainContext.setClassLoader(this.getClass().getClassLoader());
        mainContext.register(ACLFSpringConfig.class);
        mainContext.refresh();

        for (Map.Entry<ClassLoader, AddonDefinition> entry : addonDefinitionHashMap.entrySet()) {
            AddonDefinition addonDefinition = entry.getValue();
            ClassLoader addonClassLoader = entry.getKey();

            AnnotationConfigApplicationContext addonContext = new AnnotationConfigApplicationContext();

            addonContext.setClassLoader(addonClassLoader);
            addonContext.register(addonDefinition.getConfigurationClass());

            addonContext.setParent(mainContext);
            addonContext.refresh();
        }

    }

    private HashMap<ClassLoader, AddonDefinition> getAddonDefinitionMap() throws ACLFCriticalException {
        final Set<Class<?>> classes = scanForAddonsConfigurationClassesSet();
        final HashMap<ClassLoader, AddonDefinition> hashMap = new HashMap<>();

        for (Class<?> configurationClass : classes) {
            ClassLoader configurationClassLoader = configurationClass.getClassLoader();
            Plugin plugin = pluginHashMap.get(configurationClassLoader);

            AddonDefinition definition = hashMap.get(configurationClassLoader);
            //region throw if configuration is already defined
            if (definition != null) {
                String errorMessage = "Configuration for " +
                        ChatColor.GOLD + definition.getPlugin().getName() + ChatColor.RED + " is already defined in " +
                        definition.getConfigurationClass().getSimpleName() + ".class";
                throw new ACLFCriticalException(errorMessage);
            }
            //endregion

            definition = new AddonDefinition(configurationClassLoader, plugin, configurationClass);
            hashMap.put(configurationClassLoader, definition);
        }

        return hashMap;
    }

    /**
     * Finds classloader for every dependent plugin.
     * Fills {@link #pluginHashMap}
     */
    private HashMap<ClassLoader, Plugin> scanForClassLoaders() {
        return dependentPlugins.stream()
                .collect(Collectors
                        .toMap(
                                plugin -> plugin.getClass().getClassLoader(),
                                o -> o,
                                (a, b) -> b,
                                HashMap::new
                        )
                );
    }

    /**
     * Scans all plugins, looking for dependencies on ACLF.
     * Add dependent plugins to hash map
     *
     * Code from 'plugin.yml'
     * <code>
     *     depend:
     *      - ACLFramework
     * </code>
     */
    private List<Plugin> findDependentPlugins() {
        return Arrays.stream(Bukkit.getPluginManager().getPlugins())
                .filter(plugin -> plugin.getDescription().getDepend().contains(ACLF.getACLF().getName()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public int getCandidatesCount() {
        return dependentPlugins.size();
    }

    public List<Plugin> getDependentPlugins() {
        return ImmutableList.copyOf(dependentPlugins);
    }

    /**
     * TODO - javadoc
     */
    private Set<Class<?>> scanForAddonsConfigurationClassesSet() {
        final Set<Class<?>> configurationClasses = new LinkedHashSet<>();

        for (Plugin plugin : dependentPlugins) {
            String mainClassPackage = ReflectionUtils.packageFromClassName(plugin.getDescription().getMain());
            Reflections reflections = ReflectionUtils.getReflectionsFor(plugin.getClass().getClassLoader(), mainClassPackage);
            Set<Class<?>> packageClasses = reflections.getTypesAnnotatedWith(ACLFConfiguration.class);
            configurationClasses.addAll(packageClasses);
        }

        return configurationClasses;
    }

}
