package io.github.xaxvs.projdiscordbot.bot.commands;

import io.github.xaxvs.projdiscordbot.file.SaveData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public class SetUpdateChannel implements BotCommand {
    private final SaveData saveData;
    private final Path path;

    public SetUpdateChannel(SaveData saveData, Path path) {
        this.saveData = saveData;
        this.path = path;
    }

    @Override
    public void onCommandReceived(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();
        String channelId = event.getChannelId();
        saveData.setUpdateChannelId(channelId);
        event.getHook().sendMessage("Successfully set the channel to watch for the command").setEphemeral(true).queue();
        saveData.save(path);
    }

    @Override
    public String getName() {
        return "setupdatechannel";
    }

    @Override
    public String getDescription() {
        return "Sets the channel to listen for the command";
    }

    @Override
    public Collection<? extends OptionData> getOptionData() {
        return List.of();
    }
}
