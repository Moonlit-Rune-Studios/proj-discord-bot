package io.github.xaxvs.projdiscordbot;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.xaxvs.projdiscordbot.bot.Bot;
import io.github.xaxvs.projdiscordbot.bot.listeners.BotReady;
import io.github.xaxvs.projdiscordbot.bot.listeners.BotShutdown;
import io.github.xaxvs.projdiscordbot.bot.listeners.ListenerManager;
import io.github.xaxvs.projdiscordbot.file.SaveData;
import io.github.xaxvs.projdiscordbot.github.GithubClient;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

//GithubNotifs webhook user-id 1561274064621011045

public class Main {

  private static final JsonMapper mapper = new JsonMapper();
  private static final Logger log = LoggerFactory.getLogger(Main.class);
  private static final Dotenv dotenv = Dotenv.load();
  private final static ExecutorService service = Executors.newSingleThreadExecutor();
  public static GithubClient githubClient;
  private static Path appDir;
  private final ListenerManager listenerManager = new ListenerManager();
  private final Map<Bot, Path> botInstances = new HashMap<>();
  private final Timer timer = new Timer("Bot Tasks");
  private final TimerTask saver;

  public Main(JDA jda) {
    saver = new TimerTask() {
      @Override
      public void run() {
        saveAllData();
      }
    };

    jda.addEventListener(listenerManager);
    jda.addEventListener(new BotShutdown(unused -> {
      timer.cancel();
      saver.cancel();
      saveAllData();
    }));
    for (Guild guild : jda.getGuildCache().stream().toList()) {
      loadBot(guild.getId(), jda);
    }

    timer.scheduleAtFixedRate(saver, 0, 1000 * 60 * 5L);

  }

  public static void getReadme(Consumer<GithubClient.ReadmeSnapshot> readme) {
    service.execute(() -> {
      GithubClient.ReadmeSnapshot snapshot = githubClient.getReadme(
          "Moonlit-Rune-Studios", "Domum"
      );
      if (snapshot == null) readme.accept(null);
      readme.accept(snapshot);
    });
  }

  public static void main(String[] args) {

    try {
      githubClient = new GithubClient(
          dotenv.get("GITHUB_APP_ID"),
          Long.parseLong(dotenv.get("GITHUB_APP_INSTALLATION_ID")),
          Path.of(dotenv.get("GITHUB_KEY_PATH"))
      );
    } catch (IOException | GeneralSecurityException e) {
      log.error("Failed to load github client!", e);
      return;
    }

    Path jarPath;
    try {
      jarPath = Path.of(
          Main.class.getProtectionDomain()
              .getCodeSource()
              .getLocation()
              .toURI()
      );
    } catch (URISyntaxException e) {
      log.error("Failed to locate working directory.", e);
      return;
    }

    appDir = Files.isDirectory(jarPath)
        ? jarPath
        : jarPath.getParent();


    String token = dotenv.get("BOT_TOKEN");
    if (token == null) {
      log.error("Bot token is null. Check the .env variable...");
      return;
    }

    JDABuilder.createDefault(token)
        .enableIntents(EnumSet.allOf(GatewayIntent.class))
        .addEventListeners(new BotReady(Main::new))
        .build();
  }

  private void saveAllData() {
    botInstances.forEach((bot, path) -> {
      try {
        mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), bot.getSaveData());
      } catch (IOException e) {
        log.error("Failed to save for guild: {}", bot.getGuildId(), e);
      }
    });
  }

  private void loadBot(String guildId, JDA jda) {
    SaveData saveData = null;
    Path directory = appDir.resolve(guildId);
    if (!Files.exists(directory)) {
      try {
        Files.createDirectory(directory);
      } catch (IOException e) {
        log.error("Failed to create {} directory", guildId, e);
        return;
      }
    }
    Path fileSave = appDir.resolve(guildId + "/save_data.json");
    if (!Files.exists(fileSave)) {
      try {
        Files.createFile(fileSave);
        log.info("Created new save data for guild: {}", guildId);
        saveData = new SaveData();
        mapper.writerWithDefaultPrettyPrinter().writeValue(fileSave.toFile(), saveData);
      } catch (IOException e) {
        log.error("Failed to create {}/save_data.json", guildId, e);
        return;
      }
    }
    if (saveData == null) {
      try {
        saveData = mapper.readValue(fileSave.toFile(), SaveData.class);
        log.info("Loaded save data for guild: {}", guildId);
      } catch (IOException e) {
        log.error("Failed to parse {}", fileSave.toAbsolutePath(), e);
        return;
      }
    }
    botInstances.put(new Bot(jda, guildId, saveData, listenerManager, fileSave), fileSave);
  }
}
