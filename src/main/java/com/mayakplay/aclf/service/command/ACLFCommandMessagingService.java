package com.mayakplay.aclf.service.command;

import com.mayakplay.aclf.ACLF;
import com.mayakplay.aclf.annotation.Documented;
import com.mayakplay.aclf.definition.ArgumentDefinition;
import com.mayakplay.aclf.definition.CommandDefinition;
import com.mayakplay.aclf.definition.CommandDescriptionDefinition;
import com.mayakplay.aclf.service.translation.TranslationService;
import com.mayakplay.aclf.type.ArgumentMistakeType;
import com.mayakplay.aclf.type.CommandProcessOutput;
import com.mayakplay.aclf.util.StaticUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 27.06.2019.
 */
@Service
@AllArgsConstructor
public class ACLFCommandMessagingService implements CommandMessagingService {

    private final TranslationService translationService;

    @SneakyThrows
    private String getLanguage(@NotNull Player p){
        Method getHandle = getGetHandleMethod(p.getClass());
        if (getHandle == null) return ACLF.getServerLocale().getLanguage();

        Object ep = getHandle.invoke(p, (Object[]) null);
        Field f = ep.getClass().getDeclaredField("locale");
        f.setAccessible(true);
        return (String) f.get(ep);
    }

    private Method getGetHandleMethod(Class<?> clazz) {
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals("getHandle"))
                return m;
        }
        return null;
    }

    @Override
    public void sendResponseMessage(@NotNull CommandProcessOutput commandProcessOutput,
                                    @NotNull CommandDefinition commandDefinition,
                                    @NotNull CommandSender sender,
                                    @Nullable String exceptionMessage,
                                    @Nullable List<ArgumentMistakeType> mistakeTypes) {
        CommandDescriptionDefinition description = commandDefinition.getCommandDescriptionScanner();

        Locale locale = ACLF.getServerLocale();
        if (sender instanceof Player) {
            String languageTag = getLanguage((Player) sender);
            try {
                locale = LocaleUtils.toLocale(languageTag);
            } catch (Exception ignored) {
                locale = Locale.forLanguageTag(languageTag);
            }
            System.out.println("Player locale is " + locale.getDisplayName(locale));
        }

        Plugin plugin = commandDefinition.getPlugin();

        switch (commandProcessOutput) {
            case NO_PERMISSIONS:
                send(sender, translationService.getTranslated(plugin, description.getNoPermissionsMessage(), locale));
                break;
            case OPS_ONLY:
                send(sender, translationService.getTranslated(plugin, description.getOpsOnlyMessage(), locale));
                break;
            case PLAYERS_ONLY:
                send(sender, translationService.getTranslated(plugin, description.getPlayersOnlyMessage(), locale));
                break;
            case CONSOLE_ONLY:
                send(sender, translationService.getTranslated(plugin, description.getConsoleOnlyMessage(), locale));
                break;
            case CHAT_ONLY:
                send(sender, translationService.getTranslated(plugin, description.getChatOnlyMessage(), locale));
                break;
            case CHANNEL_ONLY:
                send(sender, translationService.getTranslated(plugin, description.getChannelOnlyMessage(), locale));
                break;
            case INVALID_ARGUMENTS:
                send(sender, getUsageMessage(commandDefinition, mistakeTypes, plugin, locale));
                break;
            case EXCEPTION:
                if (exceptionMessage != null)
                    send(sender, exceptionMessage);
                break;
        }

    }

    private void send(CommandSender sender, @NotNull String message) {
        sender.sendMessage(ChatColor.RED + StaticUtils.replceColors(message));
    }

    @Override
    public String getUsageMessage(@NotNull CommandDefinition commandDefinition, @Nullable List<ArgumentMistakeType> argumentMistakeTypes, @NotNull Plugin plugin, @NotNull Locale locale) {
        String translationKey = commandDefinition.getCommandDescriptionScanner().getUsageMessage();
        String translated = translationService.getTranslated(plugin, translationKey, locale);

        return StringUtils.replaceEach(translated,
                        new String[]{Documented.COMMAND_NAME, Documented.ARGUMENTS_USAGE_PARAM},
                        new String[]{getCommandName(commandDefinition), getArguments(commandDefinition, argumentMistakeTypes)});
    }

    private String getCommandName(CommandDefinition definition) {
        return definition.getCommandName().replace(":", " ").trim();
    }

    private String getArguments(CommandDefinition definition, @Nullable List<ArgumentMistakeType> mistakeTypes) {
        if (mistakeTypes == null) {
            mistakeTypes = new ArrayList<>();
            for (int i = 0; i < definition.getArgumentDefinitions().size(); i++) {
                mistakeTypes.add(ArgumentMistakeType.NOT_SPECIFIED);
            }
        }

        StringBuilder argumentsStringBuilder = new StringBuilder();

        final List<ArgumentDefinition> stringParsedArgumentDefinitionList = definition.getArgumentDefinitions().stream()
                .filter(ArgumentDefinition::isResponseArgument)
                .collect(Collectors.toList());

        for (int i = 0; i < stringParsedArgumentDefinitionList.size(); i++) {
            ArgumentMistakeType mistakeType = mistakeTypes.get(i) != null ? mistakeTypes.get(i) : ArgumentMistakeType.NOT_SPECIFIED;
            ArgumentDefinition argumentDefinition = stringParsedArgumentDefinitionList.get(i);

            argumentsStringBuilder
                    .append(mistakeType.getChatColor())
                    .append("[")
                    .append(ChatColor.stripColor(argumentDefinition.getName()))
                    .append("] ");
        }

        return argumentsStringBuilder.toString().trim();
    }

}