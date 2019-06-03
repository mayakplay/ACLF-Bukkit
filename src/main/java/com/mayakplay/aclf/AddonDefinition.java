package com.mayakplay.aclf;

import com.google.common.base.Objects;
import org.bukkit.plugin.Plugin;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

/**
 * @author mayakplay
 * @version 0.1.1
 * @since 24.05.2019.
 */
public final class AddonDefinition {

    private boolean isCompleted = false;

    private ClassLoader classLoader;
    private Plugin plugin;
    private Class<?> configurationClass;
    private AnnotationConfigApplicationContext context;

    private List<Object> resources;

    public AddonDefinition(ClassLoader classLoader, Plugin plugin, Class<?> configurationClass) {
        this.classLoader = classLoader;
        this.plugin = plugin;
        this.configurationClass = configurationClass;
    }

    public void setContext(AnnotationConfigApplicationContext context) {
        if (this.context == null)
            this.context = context;
    }

    public AnnotationConfigApplicationContext getContext() {
        return context;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Class<?> getConfigurationClass() {
        return configurationClass;
    }

    //region Description
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddonDefinition that = (AddonDefinition) o;
        return Objects.equal(classLoader, that.classLoader);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(classLoader);
    }
    //endregion
}