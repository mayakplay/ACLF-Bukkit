package com.mayakplay.aclf.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author mayakplay
 * @version 0.1.1
 * @since 25.05.2019.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Resource {}