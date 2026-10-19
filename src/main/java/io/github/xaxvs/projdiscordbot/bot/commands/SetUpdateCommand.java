package io.github.xaxvs.projdiscordbot.bot.commands;

import io.github.xaxvs.projdiscordbot.file.SaveData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public class SetUpdateCommand implements BotCommand {

  private final SaveData saveData;
  private final Path path;

  public SetUpdateCommand(SaveData saveData, Path path) {
    this.saveData = saveData;
    this.path = path;
  }

  @Override
  public void onCommandReceived(SlashCommandInteractionEvent event) {
    event.deferReply(true).queue();
    String option = event.getOption("user_id").getAsString();
    saveData.setUpdateCommand(option);
    saveData.save(path);
    event.getHook().sendMessage("Successfully set the user id").queue();
  }

  @Override
  public String getName() {
    return "setupdatecommand";
  }

  @Override
  public String getDescription() {
    return "Sets the command to listen for that will re-fetch the readme";
  }

  @Override
  public Collection<? extends OptionData> getOptionData() {
    return List.of(new OptionData(
        OptionType.STRING,
        "command",
        "the command, ex: $README",
        true));
  }
}
