package io.github.xaxvs.projdiscordbot.bot.listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.function.Consumer;

public class BotReady extends ListenerAdapter {

    private final Consumer<JDA> callback;

    public BotReady(Consumer<JDA> callback) {
        this.callback = callback;
    }

    @Override
    public void onReady(ReadyEvent event) {
        callback.accept(event.getJDA());
    }
}
