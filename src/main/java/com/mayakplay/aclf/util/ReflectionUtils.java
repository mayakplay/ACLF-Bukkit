package com.mayakplay.aclf.util;

import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.Objects;

/**
 * @author mayakplay
 * @version 0.1.1
 * @since 25.05.2019.
 */
public class ReflectionUtils {

    @Nullable
    public static Class<?> getMethodCallerClass() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        try {
            return Class.forName(stackTrace[3].getClassName());
        } catch (ClassNotFoundException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(Objects.requireNonNull(getMethodCallerClass()).getSimpleName());
    }

    public static Reflections getReflectionsFor(ClassLoader classLoader, String mainClassPackage) {
        return new Reflections(new ConfigurationBuilder()
                .setScanners(
                        new SubTypesScanner(false),
                        new ResourcesScanner(),
                        new TypeAnnotationsScanner())
                .setUrls(
                        ClasspathHelper.forClassLoader(classLoader))
                .filterInputsBy(
                        new FilterBuilder().include(FilterBuilder.prefix(mainClassPackage)))
        );
    }

    public static String packageFromClassName(String className) {
        String s;
        try {
            s = Class.forName(className).getPackage().getName();
        } catch (ClassNotFoundException e) {
            s = "";
        }
        return s;
    }

}
