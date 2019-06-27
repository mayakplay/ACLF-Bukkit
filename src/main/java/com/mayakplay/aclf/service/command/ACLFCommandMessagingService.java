package com.mayakplay.aclf.service.command;

import com.mayakplay.aclf.annotation.Documented;
import com.mayakplay.aclf.definition.ArgumentDefinition;
import com.mayakplay.aclf.definition.CommandDefinition;
import com.mayakplay.aclf.definition.CommandDescriptionDefinition;
import com.mayakplay.aclf.service.translation.TranslationService;
import com.mayakplay.aclf.type.ArgumentMistakeType;
import com.mayakplay.aclf.type.CommandProcessOutput;
import com.mayakplay.aclf.util.StaticUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

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
public class ACLFCommandMessagingService implements CommandMessagingService {

    private TranslationService translationService;

    @Override
    public void sendResponseMessage(@NotNull CommandProcessOutput commandProcessOutput,
                                    @NotNull CommandDefinition commandDefinition,
                                    @NotNull CommandSender sender,
                                    @Nullable String exceptionMessage,
                                    @Nullable List<ArgumentMistakeType> mistakeTypes) {
        CommandDescriptionDefinition description = commandDefinition.getCommandDescriptionScanner();
        switch (commandProcessOutput) {
            case NO_PERMISSIONS:
                send(sender, description.getNoPermissionsMessage());
                break;
            case OPS_ONLY:
                send(sender, description.getOpsOnlyMessage());
                break;
            case PLAYERS_ONLY:
                send(sender, description.getPlayersOnlyMessage());
                break;
            case CONSOLE_ONLY:
                send(sender, description.getPlayersOnlyMessage());
                break;
            case CHAT_ONLY:
                send(sender, description.getChatOnlyMessage());
                break;
            case CHANNEL_ONLY:
                send(sender, description.getChannelOnlyMessage());
                break;
            case INVALID_ARGUMENTS:
                send(sender, getUsageMessage(commandDefinition, mistakeTypes));
                break;
            case EXCEPTION:
                send(sender, exceptionMessage);
                break;
        }

    }

    private void send(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.RED + StaticUtils.replceColors(message));
    }

    @Override
    public String getUsageMessage(@NotNull CommandDefinition commandDefinition, @Nullable List<ArgumentMistakeType> argumentMistakeTypes) {
        String translationKey = commandDefinition.getCommandDescriptionScanner().getUsageMessage();
        String translated = translationService.getTranslated(commandDefinition.getPlugin(), translationKey, Locale.ENGLISH);

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
                    .append(argumentDefinition.getName())
                    .append("] ");
        }

        return argumentsStringBuilder.toString().trim();
    }

}