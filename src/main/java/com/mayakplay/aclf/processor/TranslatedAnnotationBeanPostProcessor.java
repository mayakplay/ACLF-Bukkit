package com.mayakplay.aclf.processor;

import com.mayakplay.aclf.ACLF;
import com.mayakplay.aclf.annotation.Translated;
import com.mayakplay.aclf.service.interfaces.TranslationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

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

            if (annotation != null) {
                String translatedKey = annotation.value();

                if (translatedKey.isEmpty())
                    translatedKey = field.getName().replace('_', '.').toLowerCase();

                translationService.getTranslated(ACLF.getPluginByClass(bean.getClass()), translatedKey);
            }
        }

        return bean;
    }

}
