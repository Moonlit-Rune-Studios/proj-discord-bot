package io.github.xaxvs.projdiscordbot.bot;

import io.github.xaxvs.projdiscordbot.bot.commands.BotCommand;
import io.github.xaxvs.projdiscordbot.bot.commands.SetWebhookChannel;
import io.github.xaxvs.projdiscordbot.bot.listeners.BotListener;
import io.github.xaxvs.projdiscordbot.bot.listeners.Listener;
import io.github.xaxvs.projdiscordbot.file.SaveData;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Bot implements CommandMap {

    private final JDA bot;
    private final SaveData saveData;
    private final String guildId;
    private final BotEventHandler botEventHandler;
    private final Map<String, BotCommand> botCommands = new HashMap<>();
    private final Listener listener;
    private final Path path;

    public Bot(JDA bot, String guildId, SaveData saveData, Listener listener, Path path) {
        this.bot = bot;
        this.saveData = saveData;
        this.guildId = guildId;
        this.listener = listener;
        botEventHandler = new BotEventHandler(saveData, this);
        this.path = path;

        addCommand(new SetWebhookChannel(saveData, path));
        initCommands();

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

    public Path getPath() {
        return path;
    }

    private void addCommand(BotCommand command) {
        botCommands.put(command.getName().toLowerCase(Locale.ROOT), command);
    }

    private void initCommands() {
        Guild guild = bot.getGuildById(guildId);
        for (BotCommand cmd : botCommands.values()) {
            guild.upsertCommand(
                    Commands.slash(cmd.getName(),
                                    cmd.getDescription())
                            .addOptions(cmd.getOptionData())
            ).queue();
        }
    }

    public BotEventHandler getBotEventHandler() {
        return botEventHandler;
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
