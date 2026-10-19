package io.github.xaxvs.projdiscordbot.bot;

import io.github.xaxvs.projdiscordbot.Main;
import io.github.xaxvs.projdiscordbot.file.SaveData;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class BotEventHandler {

  private static final Logger log = LoggerFactory.getLogger("BOT");
  private final SaveData saveData;
  private final CommandMap commands;
  private long lastExecuted = 0;

  public BotEventHandler(SaveData saveData, CommandMap commands) {
    this.saveData = saveData;
    this.commands = commands;
  }

  public void onCommandSlashEvent(SlashCommandInteractionEvent event) {
    commands.getBotCommands().get(
        event.getName().toLowerCase(Locale.ROOT)
    ).onCommandReceived(event);
  }

  public void onMessageReceived(MessageReceivedEvent event) {
    if (!SaveData.isValid(saveData)) return;
    String channelId = saveData.getUpdateChannelId();
    String updateCommand = saveData.getUpdateCommand();
    Message message = event.getMessage();
    if (!message.getChannelId().equals(channelId)) return;
    String content = message.getContentDisplay();
    if (!content.equals(updateCommand)) return;
    long currentTime = System.nanoTime();
    if (currentTime - lastExecuted < saveData.getReadmeCommandRateLimit()) return;
    final TextChannel channel = event.getGuild().getTextChannelById(
        saveData.getUpdateChannelId());
    if (channel == null) return;

    message.delete().queue(
        a -> {
          lastExecuted = System.nanoTime();
          Main.getReadme(snapshot -> {
            if (snapshot == null) {
              log.error("Failed to retrieve readme");
              return;
            }
            sendMessage(channel, snapshot.content());
          });
        },
        throwable -> log.error("Error: Failed to delete message", throwable)
    );

  }

  private void sendMessage(TextChannel guildChannel, String message) {
    guildChannel.sendMessage(message).queue();
  }


}
