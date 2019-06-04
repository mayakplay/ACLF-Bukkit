package com.mayakplay.aclf.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 03.06.2019.
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Translated {

    String value() default "";

}
