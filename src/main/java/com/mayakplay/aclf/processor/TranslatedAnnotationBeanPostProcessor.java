package com.mayakplay.aclf.processor;

import com.mayakplay.aclf.ACLF;
import com.mayakplay.aclf.annotation.Translated;
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
public class TranslatedAnnotationBeanPostProcessor implements BeanPostProcessor {

    private final TranslationService translationService;

    @Autowired
    public TranslatedAnnotationBeanPostProcessor(TranslationService translationService) {
        this.translationService = translationService;
    }

    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, String beanName) throws BeansException {

        for (Field field : bean.getClass().getDeclaredFields()) {
            Translated annotation = field.getAnnotation(Translated.class);

            if (annotation != null && field.getType().equals(String.class)) {
                String translatedKey = annotation.value();

                if (translatedKey.isEmpty())
                    translatedKey = field.getName().replace('_', '.').toLowerCase();


                Plugin pluginByClass = ACLF.getPluginByClass(bean.getClass());

                //region Test
                System.out.println(pluginByClass + "testtesttest");
                if (pluginByClass != null)
                    System.out.println(pluginByClass.getName() + "testwqefqwefqwefqwefqwef");
                //endregion

                String translated = translationService.getTranslated(pluginByClass, translatedKey);
                if (translated == null) translated = translatedKey;

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
