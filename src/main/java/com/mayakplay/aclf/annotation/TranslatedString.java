package com.mayakplay.aclf.annotation;

import com.mayakplay.aclf.processor.TranslatedAnnotationBeanPostProcessor;
import com.mayakplay.aclf.service.translation.TranslationService;
import org.springframework.beans.factory.config.Scope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Localized string injection annotation only for "sender" {@link Scope}.
 * Field might be private, final, but not static!
 *
 * {@link ElementType#TYPE_USE} means that annotated string
 * will be translated during processing.
 *
 * @author mayakplay
 * @version 0.0.1
 * @since 03.06.2019.
 *
 * @see TranslatedAnnotationBeanPostProcessor
 * @see TranslationService
 */
@Retention(RUNTIME)
@Target({FIELD, TYPE_USE})
public @interface TranslatedString {

    /**
     * ".lang" key reference.
     * Translator will get key from filed name, if empty.
     */
    String value() default "";

}