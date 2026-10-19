package io.github.xaxvs.projdiscordbot.file;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

public class SaveData {
    private String webhookOutputChannelId;
    private String readmeOutputChannelId;
    private String readmeOutputMessageId;
    private String webhookUserId;

    public SaveData() {
        webhookOutputChannelId = " ";
        readmeOutputChannelId = " ";
        readmeOutputMessageId = " ";
        webhookUserId = " ";
    }

    public static boolean isValid(SaveData saveData) {
        return saveData.webhookUserId != null &&
                !saveData.webhookUserId.isBlank() &&
                saveData.webhookOutputChannelId != null &&
                !saveData.webhookOutputChannelId.isBlank() &&
                saveData.getReadmeOutputChannelId() != null &&
                !saveData.getReadmeOutputChannelId().isBlank() &&
                saveData.getReadmeOutputMessageId() != null &&
                !saveData.getReadmeOutputMessageId().isBlank();
    }

    public String getWebhookUserId() {
        return webhookUserId;
    }

    public void setWebhookUserId(String webhookUserId) {
        this.webhookUserId = webhookUserId;
    }

    public String getReadmeOutputChannelId() {
        return readmeOutputChannelId;
    }

    public void setReadmeOutputChannelId(String readmeOutputChannelId) {
        this.readmeOutputChannelId = readmeOutputChannelId;
    }

    public String getReadmeOutputMessageId() {
        return readmeOutputMessageId;
    }

    public void setReadmeOutputMessageId(String readmeOutputMessageId) {
        this.readmeOutputMessageId = readmeOutputMessageId;
    }

    public String getWebhookOutputChannelId() {
        return webhookOutputChannelId;
    }

    public void setWebhookOutputChannelId(String webhookOutputChannelId) {
        this.webhookOutputChannelId = webhookOutputChannelId;
    }

    public void save(Path path) {
        JsonMapper mapper = new JsonMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), this);
        } catch (IOException e) {
            LoggerFactory.getLogger("Json File Saver")
                    .error("Failed to save {}", path.toAbsolutePath(), e);
        }
    }
}
