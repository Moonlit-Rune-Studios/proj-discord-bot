package io.github.xaxvs.projdiscordbot.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Collection;

public interface BotCommand {
    void onCommandReceived(SlashCommandInteractionEvent event);

    String getName();

    String getDescription();

    Collection<? extends OptionData> getOptionData();
}
