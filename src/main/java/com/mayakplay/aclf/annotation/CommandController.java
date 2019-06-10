package com.mayakplay.aclf.annotation;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Controller type annotation
 *
 * @see CommandMapping
 * @see ChatMapping
 * @see ChannelMapping
 *
 * @see OpsOnly
 * @see Permitted
 * @see Documented
 *
 * @see Argument
 *
 * Date: 09.06.2019<br/>
 * @author Mayakplay
 */
@Target(TYPE)
@Retention(RUNTIME)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public @interface CommandController {



}