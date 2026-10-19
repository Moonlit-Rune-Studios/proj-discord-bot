package io.github.xaxvs.projdiscordbot.bot.listeners;

import net.dv8tion.jda.api.events.GenericEvent;

public interface Listener {
  <T extends GenericEvent> void add(BotListener<T> botListener, String guildId);
}
