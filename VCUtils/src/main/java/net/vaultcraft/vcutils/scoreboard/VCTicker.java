package net.vaultcraft.vcutils.scoreboard;

/**
 * Created by Connor on 9/2/14. Designed for the VCUtils project.
 */

public class VCTicker {

    private String value;
    private int tick;
    private int maxLen;

    public VCTicker(String whole, int maxLen) {
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

            return p1+p2;
        }


        return value.substring(indexMin, indexMax);
    }

    public void updateTicker(String value) {
        this.value = value;
    }
}