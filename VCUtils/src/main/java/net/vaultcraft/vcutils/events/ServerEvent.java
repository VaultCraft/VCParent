package net.vaultcraft.vcutils.events;

import org.bukkit.plugin.Plugin;

/**
 * Created by tacticalsk8er on 8/4/2014.
 */
public abstract class ServerEvent {

    private String name;
    private int frequency;
    private TimeUnit timeUnit;
    private float chance;

    public ServerEvent(String name, int frequency, TimeUnit timeUnit, float chance) {
        this.name = name;
        this.frequency = frequency;
        this.timeUnit = timeUnit;
        this.chance = chance;
    }

    public String getName() {
        return name;
    }

    public int getFrequency() {
        return frequency;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public float getChance() {
        return chance;
    }

    public abstract void onEvent(Plugin plugin);

    public abstract void onTick(Plugin plugin, int timeRemaining);
}
