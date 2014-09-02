package net.vaultcraft.vcutils.chat.fancy;

/**
 * Created by Connor on 9/1/14. Designed for the VCUtils project.
 */

public enum JSONChatHoverEventType {
    SHOW_TEXT("show_text"),
    SHOW_ITEM("show_item"),
    SHOW_ACHIEVEMENT("show_achievement");
    private final String type;

    JSONChatHoverEventType(String type) {
        this.type = type;
    }

    public String getTypeString() {
        return type;
    }
}