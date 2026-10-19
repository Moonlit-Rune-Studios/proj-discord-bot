package io.github.xaxvs.projdiscordbot.bot;

import io.github.xaxvs.projdiscordbot.bot.commands.BotCommand;

import java.util.Map;

public interface CommandMap {

    Map<String, BotCommand> getBotCommands();
}
