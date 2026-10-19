package io.github.xaxvs.projdiscordbot.bot.listeners;

import net.dv8tion.jda.api.events.GenericEvent;

import java.util.function.Consumer;

public record BotListener<T extends GenericEvent>(
    Consumer<T> callback,
    Class<T> type
) {

  public void dispatch(GenericEvent event) {
    T e = type.cast(event);
    callback.accept(e);
  }
}