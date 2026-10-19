package io.github.xaxvs.projdiscordbot.file;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

public class SaveData {
    private String updateChannelId;
    private String readmeOutputChannelId;
    private String updateCommand;
    private long readmeCommandRateLimit;

    public SaveData() {
        updateChannelId = " ";
        readmeOutputChannelId = " ";
        updateCommand = " ";
        readmeCommandRateLimit = 0;
    }

    public static boolean isValid(SaveData saveData) {
        return saveData.updateCommand != null &&
                !saveData.updateCommand.isBlank() &&
                saveData.updateChannelId != null &&
                !saveData.updateChannelId.isBlank() &&
                saveData.getReadmeOutputChannelId() != null &&
                !saveData.getReadmeOutputChannelId().isBlank();
    }

    public long getReadmeCommandRateLimit() {
        return readmeCommandRateLimit;
    }

    public void setReadmeCommandRateLimit(long readmeCommandRateLimit) {
        this.readmeCommandRateLimit = readmeCommandRateLimit;
    }

    public String getUpdateCommand() {
        return updateCommand;
    }

    public void setUpdateCommand(String updateCommand) {
        this.updateCommand = updateCommand;
    }

    public String getReadmeOutputChannelId() {
        return readmeOutputChannelId;
    }

    public void setReadmeOutputChannelId(String readmeOutputChannelId) {
        this.readmeOutputChannelId = readmeOutputChannelId;
    }

    public String getUpdateChannelId() {
        return updateChannelId;
    }

    public void setUpdateChannelId(String updateChannelId) {
        this.updateChannelId = updateChannelId;
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
