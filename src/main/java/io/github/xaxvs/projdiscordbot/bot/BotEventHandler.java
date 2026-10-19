package io.github.xaxvs.projdiscordbot.bot;

import io.github.xaxvs.projdiscordbot.file.SaveData;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class BotEventHandler {

  private static final Logger log = LoggerFactory.getLogger("BOT");
  private final SaveData saveData;
  private final CommandMap commands;

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
    String rmOutChannelId = saveData.getReadmeOutputChannelId();
    String webhookId = saveData.getWebhookUserId();
    if (!event.isWebhookMessage()) return;
    Message message = event.getMessage();
    if (!message.getChannelId().equals(rmOutChannelId)) return;
    if (!message.getAuthor().getId().equals(webhookId)) return;
    String content = message.getContentDisplay();
    if (!content.equals("README")) return;
    var result = message.delete();
    result.queue(
        a -> log.info("Success"),
        throwable -> log.error("Error: Failed to delete message", throwable)
    );
  }

}
