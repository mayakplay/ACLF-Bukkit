package com.mayakplay.aclf.processor;

import com.mayakplay.aclf.ACLF;
import com.mayakplay.aclf.annotation.TranslatedString;
import com.mayakplay.aclf.service.interfaces.TranslationService;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 03.06.2019.
 */
@Component
public class TranslatedAnnotationBeanPostProcessor<T> implements BeanPostProcessor {

    private final TranslationService translationService;

    @Autowired
    public TranslatedAnnotationBeanPostProcessor(TranslationService translationService) {
        this.translationService = translationService;
    }

    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, String beanName) throws BeansException {

        for (Field field : bean.getClass().getDeclaredFields()) {
            TranslatedString annotation = field.getAnnotation(TranslatedString.class);

            if (annotation != null && field.getType().equals(String.class)) {
                if (!Modifier.isFinal(field.getModifiers())) {
                    System.out.println("TODO: translation warning: field \"" + bean.getClass().getSimpleName() + "." + field.getName() + "\" must be final");
                }

                String translatedKey = annotation.value();

                if (translatedKey.isEmpty())
                    translatedKey = field.getName().replace('_', '.').toLowerCase();


                Plugin pluginByClass = ACLF.getPluginByClass(bean.getClass());

                String translated = translationService.getTranslated(pluginByClass, translatedKey);
                if (translated == null) {
                    System.out.println("TODO: translation warning: translation for key \"" + translatedKey + "\" does not found");
                    translated = translatedKey;
                }

                try {
                    //region Взлом жопы
                    field.setAccessible(true);

                    Field modifiersField = Field.class.getDeclaredField("modifiers");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

                    field.set(bean, translated);
                    //endregion

                    System.out.println(bean.getClass().getSimpleName() + "." + field.getName() + " = " + translated);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }

            }
        }

        return bean;
    }

}
