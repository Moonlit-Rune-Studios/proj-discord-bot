package io.github.xaxvs.projdiscordbot.bot.commands;

import io.github.xaxvs.projdiscordbot.file.SaveData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SetReadmeCommandRateLimit implements BotCommand {

  private final SaveData saveData;
  private final Path path;

  public SetReadmeCommandRateLimit(SaveData saveData, Path path) {
    this.saveData = saveData;
    this.path = path;
  }

  @Override
  public void onCommandReceived(SlashCommandInteractionEvent event) {
    event.deferReply(true).queue();
    long value;
    try {
      value = event.getOption("rate_limit").getAsLong();
    } catch (Exception e) {
      event.getHook()
          .setEphemeral(true)
          .sendMessage("The rate must be a number!")
          .queue();
      return;
    }
    if (value < 0) {
      event.getHook()
          .setEphemeral(true)
          .sendMessage("The rate limit must be greater than 0!")
          .queue();
      return;
    }
    saveData.setReadmeCommandRateLimit(TimeUnit.SECONDS.toNanos(value));
    saveData.save(path);
    event.getHook()
        .setEphemeral(true)
        .sendMessage("Successfully set the rate limit")
        .queue();
  }

  @Override
  public String getName() {
    return "setreadmecommandratelimit";
  }

  @Override
  public String getDescription() {
    return "Set the rate limit for the readme update command";
  }

  @Override
  public Collection<? extends OptionData> getOptionData() {
    return List.of(new OptionData(OptionType.INTEGER, "rate_limit", "The rate limit in seconds", true));
  }
}
