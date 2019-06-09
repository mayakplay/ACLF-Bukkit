package com.mayakplay.aclf.annotation;

import com.mayakplay.aclf.stereotype.ArgumentParser;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Mapped parameter annotation
 *
 * Date: 16.04.2019<br/>
 * @author Mayakplay
 */
@Target(PARAMETER)
@Retention(RUNTIME)
@Documented
public @interface Argument {

    /**
     * The name of the argument to be displayed at the usage hint.</br>
     * If value is empty returns parameter type simple name - {@link Class#getSimpleName()}
     */
    String name() default "";

    /**
     *
     */
    Class<? extends ArgumentParser> processor();

}