package com.mayakplay.aclf.annotation;

import com.mayakplay.aclf.type.MappingPrivacy;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Documented at {@link CommandController}
 *
 * Date: 09.06.2019<br/>
 * @author Mayakplay
 */
@Retention(RUNTIME)
@Target({METHOD, ANNOTATION_TYPE, TYPE})
public @interface CommandMapping {

    String value() default "";

    MappingPrivacy privacy() default MappingPrivacy.BOTH;

}