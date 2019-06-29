package com.mayakplay.aclf.definition;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 29.06.2019.
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class LocaleContainer {

    @NotNull private final Locale locale;
    @NotNull private final HashMap<String, String> keyTranslationHashMap;

    @Nullable
    String getByKey(@NotNull String key) {
        Objects.requireNonNull(key);

        System.out.println(key + "[" + key.length() + "]");

        return keyTranslationHashMap.get(key);
    }

    int getTranslationsCount() {
        return keyTranslationHashMap.size();
    }

}