package io.github.xaxvs.projdiscordbot.file;

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
}
