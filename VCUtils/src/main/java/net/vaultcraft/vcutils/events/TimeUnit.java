package net.vaultcraft.vcutils.events;

/**
 * Created by tacticalsk8er on 8/4/2014.
 */
public enum TimeUnit {

    TICKS(1),
    SECONDS(20),
    MINUTES(1200),
    HOURS(72000);


    private int modifier;

    private TimeUnit(int modifier) {
        this.modifier = modifier;
    }

    public int getModifier() {
        return modifier;
    }
}
