package io.github.xaxvs.projdiscordbot.github;

import org.kohsuke.github.GHAppInstallation;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.extras.authorization.JWTTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.GeneralSecurityException;

public class GithubClient {

  private static final Logger log = LoggerFactory.getLogger(GithubClient.class);
  private final GitHub gitHubClient;

  public GithubClient(String id, long installationId, Path path) throws IOException, GeneralSecurityException {

    JWTTokenProvider jwtProvider = new JWTTokenProvider(id, path);

    GitHub appClient = new GitHubBuilder()
        .withAuthorizationProvider(jwtProvider)
        .build();

    GHAppInstallation installation =
        appClient.getApp().getInstallationById(installationId);

    String installationToken =
        installation.createToken().create().getToken();

    gitHubClient = new GitHubBuilder()
        .withAppInstallationToken(installationToken)
        .build();
  }

  public ReadmeSnapshot getReadme(String repoOwner, String repoName) {
    GHContent readme;
    try {
      readme = gitHubClient.getRepository(repoOwner + "/" + repoName).getReadme();
    } catch (IOException e) {
      log.error("Failed to get repository", e);
      return null;
    }

    try (InputStream input = readme.read()) {
      String content = new String(
          input.readAllBytes(),
          StandardCharsets.UTF_8
      );

      return new ReadmeSnapshot(
          readme.getPath(),
          readme.getSha(),
          content
      );
    } catch (IOException e) {
      log.error("Failed to write readme content", e);
      return null;
    }
  }

  public record ReadmeSnapshot(
      String path,
      String sha,
      String content
  ) {
  }
}
