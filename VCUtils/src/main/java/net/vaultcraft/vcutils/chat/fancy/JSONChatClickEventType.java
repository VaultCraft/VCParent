package net.vaultcraft.vcutils.chat.fancy;

/**
 * Created by Connor on 9/1/14. Designed for the VCUtils project.
 */

public enum JSONChatClickEventType {
    RUN_COMMAND("run_command"),
    SUGGEST_COMMAND("suggest_command"),
    OPEN_URL("open_url");

    private final String type;

    JSONChatClickEventType(String type) {
        this.type = type;
    }

    public String getTypeString() {
        return type;
    }
}