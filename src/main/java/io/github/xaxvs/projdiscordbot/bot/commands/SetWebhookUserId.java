package io.github.xaxvs.projdiscordbot.bot.commands;

import io.github.xaxvs.projdiscordbot.file.SaveData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public class SetWebhookUserId implements BotCommand {

  private final SaveData saveData;
  private final Path path;

  public SetWebhookUserId(SaveData saveData, Path path) {
    this.saveData = saveData;
    this.path = path;
  }

  @Override
  public void onCommandReceived(SlashCommandInteractionEvent event) {
    event.deferReply(true).queue();
    String option = event.getOption("user_id").getAsString();
    saveData.setWebhookUserId(option);
    saveData.save(path);
    event.getHook().sendMessage("Successfully set the user id").queue();
  }

  @Override
  public String getName() {
    return "setwebhookuserid";
  }

  @Override
  public String getDescription() {
    return "Sets the user id of the webhook";
  }

  @Override
  public Collection<? extends OptionData> getOptionData() {
    return List.of(new OptionData(
        OptionType.STRING,
        "user_id",
        "copy the id of the webhook discord user",
        true));
  }
}
