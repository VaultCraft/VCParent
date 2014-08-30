package net.vaultcraft.vcutils.chat.bot;

/**
 * Created by Connor on 8/27/14. Designed for the VCUtils project.
 */

public class ChatterBotThought {
    private String[] emotions;
    private String text;

    public String[] getEmotions() {
        return emotions;
    }

    public void setEmotions(String[] emotions) {
        this.emotions = emotions;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}