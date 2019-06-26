package com.mayakplay.aclf.annotation;


import com.mayakplay.aclf.service.command.CommandProcessingService;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <H1>NOT A MAPPING ANNOTATION</H1>
 *
 * Annotation marks that the last argument of the command
 * will be parsed from the string that remains from parsing
 * the previous arguments.
 *
 * In USAGE message will marked as ".." double dots. [argName..]
 *
 * Will be ignored if the command contains no arguments or
 * the tagged method is not a command.
 *
 * @see CommandController
 * @see CommandProcessingService
 *
 * Date: 12.05.2019<br/>
 * @author Mayakplay
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface TailArgumentCommand {}