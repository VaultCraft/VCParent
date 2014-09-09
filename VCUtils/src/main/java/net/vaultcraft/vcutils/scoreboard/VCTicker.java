package net.vaultcraft.vcutils.scoreboard;

import org.bukkit.ChatColor;

/**
 * Created by Connor on 9/2/14. Designed for the VCUtils project.
 */

public class VCTicker {

    protected String value;
    protected int tick;
    protected int maxLen;
    protected ChatColor prefix;

    public VCTicker(String whole, int maxLen) {
        this(ChatColor.WHITE, whole, maxLen);
    }

    public VCTicker(ChatColor prefix, String whole, int maxLen) {
        this.prefix = prefix;
        this.value = whole;
        tick = 0;
        this.maxLen = maxLen;
    }

    public String tick() {
        if (value.length() <= maxLen)
            return value;

        int indexMax = tick+maxLen;
        int indexMin = tick;

        tick++;

        if (tick >= value.length())
            tick = 0;

        if (indexMax >= value.length()) {
            indexMax = Math.abs(value.length()-indexMax);

            String p1 = value.substring(indexMin);
            String p2 = value.substring(0, indexMax);

            return prefix.toString()+p1+p2;
        }


        return prefix.toString()+value.substring(indexMin, indexMax);
    }

    public void updateTicker(String value) {
        this.value = value;
    }
}