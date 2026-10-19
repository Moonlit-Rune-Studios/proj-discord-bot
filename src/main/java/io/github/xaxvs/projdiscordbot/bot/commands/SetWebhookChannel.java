package io.github.xaxvs.projdiscordbot.bot.commands;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.github.xaxvs.projdiscordbot.file.SaveData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public class SetWebhookChannel implements BotCommand{
    private static final Logger log = LoggerFactory.getLogger(SetWebhookChannel.class);
    private final SaveData saveData;
    private final Path path;
    public SetWebhookChannel(SaveData saveData, Path path) {
        this.saveData = saveData;
        this.path = path;
    }

    @Override
    public void onCommandReceived(SlashCommandInteractionEvent event) {
        log.info("HI");
        event.deferReply(true).queue();
        String channelId = event.getChannelId();
        saveData.setWebhookOutputChannelId(channelId);
        event.getHook().sendMessage("Successfully set the webhook channel id.").setEphemeral(true).queue();
        try {
            new JsonMapper().writerWithDefaultPrettyPrinter()
                    .writeValue(path.toFile(), saveData);
        } catch (IOException e) {
            log.error("Failed to save {}", path.toAbsolutePath(), e);
        }
    }

    @Override
    public String getName() {
        return "setwebhookchannel";
    }

    @Override
    public String getDescription() {
        return "Set the channel where the webhook sends its messages";
    }

    @Override
    public Collection<? extends OptionData> getOptionData() {
        return List.of();
    }
}
