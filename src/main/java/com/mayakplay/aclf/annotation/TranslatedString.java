package com.mayakplay.aclf.annotation;

import com.mayakplay.aclf.service.translation.TranslationService;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Controller localized string injection annotation only for {@link CommandController}.
 * Field might be private, final, but not static!
 *
 * @author mayakplay
 * @version 0.0.1
 * @since 03.06.2019.
 *
 * @see TranslationService
 */
@Retention(RUNTIME)
@Target({FIELD, TYPE_USE, ANNOTATION_TYPE})
public @interface TranslatedString {

    String value() default "";

}
