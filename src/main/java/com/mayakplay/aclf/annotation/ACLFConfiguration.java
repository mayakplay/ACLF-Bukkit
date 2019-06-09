package com.mayakplay.aclf.annotation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <a href="https://s00.yaplakal.com/pics/pics_original/0/6/1/6556160.jpg">Documentation link</a>
 */
@Configuration
@ComponentScan
//@Import({
//        BukkitEventListenerRegisterBeanPostProcessor.class,
//        CommandControllerRegistererBeanPostProcessor.class,
//        TranslatedAnnotationBeanPostProcessor.class
//})
@Retention(RUNTIME)
@Target(TYPE)
public @interface ACLFConfiguration {



}