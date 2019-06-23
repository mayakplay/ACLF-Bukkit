package com.mayakplay.aclf.annotation;

import com.mayakplay.aclf.type.MappingAccess;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Composed annotation that used to
 * shortcuts {@code @CommandMapping(privacy = MappingPrivacy.CHAT)}
 *
 * @see CommandMapping
 * @see CommandController
 *
 * Date: 09.06.2019<br/>
 * @author Mayakplay
 */
@Retention(RUNTIME)
@Target({METHOD, TYPE})
@CommandMapping(privacy = MappingAccess.CHAT)
public @interface ChatMapping {

    @AliasFor(annotation = CommandMapping.class, value = "value")
    String value() default "";

}