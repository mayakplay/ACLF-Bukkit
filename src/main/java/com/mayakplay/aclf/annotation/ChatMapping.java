package com.mayakplay.aclf.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Documented at {@link CommandController}
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface ChatMapping {

    String value();

}
