package com.mayakplay.aclf.annotation;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.mayakplay.aclf.annotation.CommandController.SCOPE_SENDER;
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
@Scope(SCOPE_SENDER)
@Component
public @interface CommandController {

    String SCOPE_SENDER = "sender";

}