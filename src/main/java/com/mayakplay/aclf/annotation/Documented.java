package com.mayakplay.aclf.annotation;

import com.mayakplay.aclf.service.command.CommandProcessingService;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation allows you to change the standard
 * labels displayed during the execution of a command.
 *
 * The annotation above the class will apply to
 * all methods marked with mapping annotations,
 * but annotations above the method will be in priority.
 *
 * Date: 16.04.2019<br/>
 * @author Mayakplay
 *
 * @see CommandController
 * @see CommandMapping
 * @see CommandProcessingService
 */
@Retention(RUNTIME)
@Target({METHOD, TYPE})
@java.lang.annotation.Documented
public @interface Documented {

    String ARGUMENTS_USAGE_PARAM = "{ARGUMENTS}";
    String COMMAND_NAME =          "{NAME}";

    String DEFAULT_PLAYERS_ONLY_MESSAGE =   "command.mistake.playeronly";
    String DEFAULT_CONSOLE_ONLY_MESSAGE =   "command.mistake.consoleonly";
    String DEFAULT_NO_PERMISSIONS_MESSAGE = "command.mistake.permissions";
    String DEFAULT_OPS_ONLY_MESSAGE =       "command.mistake.opsonly";
    String DEFAULT_USAGE_MESSAGE =          "command.mistake.usage";
    String DEFAULT_CHAT_ONLY_MESSAGE =      "command.mistake.chatonly";
    String DEFAULT_CHANNEL_ONLY_MESSAGE =   "command.mistake.channelonly";

    boolean displayInHelp() default false;

    /**
     * A message will be displayed when a player uses the console command.
     */
    @TranslatedString
    String playersOnlyMessage() default DEFAULT_PLAYERS_ONLY_MESSAGE;

    /**
     * The message will be displayed to the console
     * if the command has been used, is intended only for the players.
     */
    @TranslatedString
    String consoleOnlyMessage() default DEFAULT_CONSOLE_ONLY_MESSAGE;

    /**
     * This message will be displayed when
     * sender does not have enough permissions to use command
     */
    @TranslatedString
    String noPermissionsMessage() default DEFAULT_NO_PERMISSIONS_MESSAGE;

    /**
     * This message will appear when the player
     * sent the command intended for operators is not the operator
     */
    @TranslatedString
    String opsOnlyMessage() default DEFAULT_OPS_ONLY_MESSAGE;

    /**
     * The message is shown as a hint if the arguments are incorrect.
     *
     * {@value ARGUMENTS_USAGE_PARAM} will replaced with colorized arguments
     * The green color will be before the wrong argument, the red color after,
     * if no arguments are entered, the color will be orange.
     * "[arg1] [arg2] [arg3]"
     *
     * {@value COMMAND_NAME} will replaced with command name.
     * "command subCommand" or "command"
     */
    @TranslatedString
    String usage() default DEFAULT_USAGE_MESSAGE;

    /**
     * This message will be displayed when
     * player tries to use chat only command from channel
     */
    @TranslatedString
    String chatOnlyMessage() default DEFAULT_CHAT_ONLY_MESSAGE;

    /**
     * This message will be displayed when
     * player tries to use channel only command from chat
     */
    @TranslatedString
    String channelOnlyMessage() default DEFAULT_CHANNEL_ONLY_MESSAGE;

}