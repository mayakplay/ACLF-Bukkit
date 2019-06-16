package com.mayakplay.aclf.infrastructure;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Applies annotated {@link BeanPostProcessor} from main <i>spring</i> context to child contexts.
 *
 * <i>ACLF inside usage</i>
 *
 * @author mayakplay
 * @version 0.0.1
 * @since 09.06.2019.
 */
@Retention(RUNTIME)
@Target({ANNOTATION_TYPE, TYPE})
@Component
public @interface InfrastructurePostProcessor {}