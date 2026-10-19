package io.github.xaxvs.projdiscordbot.bot.listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.function.Consumer;

public class BotShutdown extends ListenerAdapter {

  private final Consumer<JDA> callback;

  public BotShutdown(Consumer<JDA> callback) {
    this.callback = callback;
  }


  @Override
  public void onShutdown(ShutdownEvent event) {
    callback.accept(event.getJDA());
  }

}
