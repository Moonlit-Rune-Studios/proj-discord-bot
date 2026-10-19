package io.github.xaxvs.projdiscordbot.bot;

import io.github.xaxvs.projdiscordbot.bot.commands.*;
import io.github.xaxvs.projdiscordbot.bot.listeners.BotListener;
import io.github.xaxvs.projdiscordbot.bot.listeners.Listener;
import io.github.xaxvs.projdiscordbot.file.SaveData;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Bot implements CommandMap {

  private static final Logger log = LoggerFactory.getLogger(Bot.class);
  private final JDA bot;
  private final SaveData saveData;
  private final String guildId;
  private final Map<String, BotCommand> botCommands = new HashMap<>();

  public Bot(JDA bot, String guildId, SaveData saveData, Listener listener, Path path) {
    this.bot = bot;
    this.saveData = saveData;
    this.guildId = guildId;
    Guild guild = bot.getGuildById(guildId);
    if (guild == null) {
      log.error("{} is null? This should not happen.", guildId);
      return;
    }
    BotEventHandler botEventHandler = new BotEventHandler(saveData, this);

    addCommand(new SetUpdateChannel(saveData, path));
    addCommand(new SetReadmeChannel(saveData, path));
    addCommand(new SetUpdateCommand(saveData, path));
    addCommand(new SetReadmeCommandRateLimit(saveData, path));
    guild.retrieveCommands(true).queue(cmds -> initCommands(guild, cmds));

    BotListener<MessageReceivedEvent> messageEvent = new BotListener<>(
        botEventHandler::onMessageReceived,
        MessageReceivedEvent.class
    );

    BotListener<SlashCommandInteractionEvent> commandEvent = new BotListener<>(
        botEventHandler::onCommandSlashEvent,
        SlashCommandInteractionEvent.class
    );
    listener.add(messageEvent, guildId);
    listener.add(commandEvent, guildId);
  }

  private void addCommand(BotCommand command) {
    botCommands.put(command.getName().toLowerCase(Locale.ROOT), command);
  }

  private void initCommands(Guild guild, List<Command> cmds) {

    for (Command cmd : cmds) {
      guild.deleteCommandById(cmd.getId()).queue();
    }

    for (BotCommand cmd : botCommands.values()) {
      log.info("added cmd {}", cmd.getName());
      guild.upsertCommand(
          Commands.slash(cmd.getName(), cmd.getDescription()).addOptions(cmd.getOptionData())
      ).queueAfter(1, TimeUnit.SECONDS);
    }
  }

  public JDA getBot() {
    return bot;
  }

  public SaveData getSaveData() {
    return saveData;
  }

  public String getGuildId() {
    return guildId;
  }

  @Override
  public Map<String, BotCommand> getBotCommands() {
    return new HashMap<>(botCommands);
  }
}
