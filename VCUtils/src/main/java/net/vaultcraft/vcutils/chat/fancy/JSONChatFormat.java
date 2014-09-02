package net.vaultcraft.vcutils.chat.fancy;

/**
 * Created by Connor on 9/1/14. Designed for the VCUtils project.
 */

public enum JSONChatFormat {
    BOLD("bold"),
    UNDERLINED("underlined"),
    STRIKETHROUGH("strikethrough"),
    ITALIC("italic"),
    OBFUSCATED("obfuscated");
    private final String format;

    JSONChatFormat(String format) {
        this.format = format;
    }

    public String getFormatString() {
        return format;
    }
}