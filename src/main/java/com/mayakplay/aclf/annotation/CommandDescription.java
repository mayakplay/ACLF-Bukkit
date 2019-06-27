package com.mayakplay.aclf.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 28.06.2019.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface CommandDescription {

    @TranslatedString
    String value();

}
