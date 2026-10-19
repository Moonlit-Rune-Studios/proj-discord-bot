package io.github.xaxvs.projdiscordbot.bot.commands;

import io.github.xaxvs.projdiscordbot.file.SaveData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public class SetReadmeChannel implements BotCommand {
  private final SaveData saveData;
  private final Path path;

  public SetReadmeChannel(SaveData saveData, Path path) {
    this.saveData = saveData;
    this.path = path;
  }

  @Override
  public void onCommandReceived(SlashCommandInteractionEvent event) {
    event.deferReply(true).queue();
    String channelId = event.getChannelId();
    saveData.setReadmeOutputChannelId(channelId);
    event.getHook().sendMessage("Successfully set the readme channel id.").setEphemeral(true).queue();
    saveData.save(path);
  }

  @Override
  public String getName() {
    return "setreadmechannel";
  }

  @Override
  public String getDescription() {
    return "Set the channel where the readme is sent";
  }

  @Override
  public Collection<? extends OptionData> getOptionData() {
    return List.of();
  }

}
