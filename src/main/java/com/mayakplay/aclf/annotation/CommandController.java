package com.mayakplay.aclf.annotation;

import com.mayakplay.aclf.pojo.ACLFPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <H1>Components:</H1>
 *
 * <H2>Reserved params:</H2>
 * <li>{@link ACLFPlayer}</li>
 * <li>{@link CommandSender}</li>
 * <li>{@link Player}</li>
 *
 * <H2>Method mapping annotations:</H2>
 * <li>{@link ChannelMapping} - will be available through channel messages</li>
 * <li>{@link ChatMapping}    - will be available via chat</li>
 * <li>{@link BothMapping}    - will be available via chat and channel</li>
 *
 * <H2>Method specification annotations:</H2>
 * <li>{@link OpsOnly}        - will be available for operators ONLY</li>
 * <li>{@link Permitted}      - command will be available if the performer has enough rights</li>
 * <li>{@link Documented}     - custom documentation</li>
 *
 * <H2>Argument annotation:</H2>
 * <li>{@link Argument}</li>
 */
@Target(TYPE)
@Retention(RUNTIME)
@Component
public @interface CommandController {

    /**
     * TODO: javadoc
     */
    String value();

}