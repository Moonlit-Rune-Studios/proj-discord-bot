package io.github.xaxvs.projdiscordbot.bot.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.Interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListenerManager extends ListenerAdapter implements Listener {

  //private final Map<Class<?>, List<BotListener<?>>> listeners = new HashMap<>();
  private final Map<Class<?>, Map<String, List<BotListener<?>>>> guildListeners = new HashMap<>();

  public <T extends GenericEvent> void listenFor(BotListener<T> event, String guildId) {
    guildListeners.putIfAbsent(event.type(), new HashMap<>());
    var classMap = guildListeners.get(event.type());
    classMap.putIfAbsent(guildId, new ArrayList<>(List.of(event)));
  }

    /*public <T extends GenericEvent> void listenFor(BotListener<T> event) {
        if (listeners.containsKey(event.type())) {
            listeners.get(event.type()).add(event);
            return;
        }

        listeners.put(event.type(), new ArrayList<>(List.of(event)));
    }*/

  @Override
  public void onGenericEvent(GenericEvent event) {

    String guildId = null;

    if (event instanceof GenericGuildEvent e) {
      guildId = e.getGuild().getId();
    }
    if (event instanceof Interaction e) {
      if (e.getGuild() != null)
        guildId = e.getGuild().getId();
    }
    if (event instanceof GenericMessageEvent e) {
      if (e.isFromGuild()) {
        guildId = e.getGuild().getId();
      }
    }

    if (guildId != null) {
      var map = guildListeners.get(event.getClass());
      if (map != null && map.containsKey(guildId)) {
        var list = map.get(guildId);
        list.forEach(botListener -> botListener.dispatch(event));
      }
    }
        /*var list = listeners.get(event.getClass());

        if (list == null) return;

        for (BotListener<? extends GenericEvent> listener : list) {
            listener.dispatch(event);
        }*/
  }

  @Override
  public <T extends GenericEvent> void add(BotListener<T> botListener, String guildId) {
    listenFor(botListener, guildId);
  }
}