package net.vaultcraft.vcutils.events;

import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tacticalsk8er on 8/4/2014.
 */
public class ServerEventHandler {

    private Plugin plugin;

    private static volatile ConcurrentHashMap<ServerEvent, Long> timeRemaining = new ConcurrentHashMap<>();

    public ServerEventHandler(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new ServerEventTask(), 1l, 1l);
    }

    public void addEvent(ServerEvent serverEvent) {
        if(timeRemaining.contains(serverEvent))
            return;
        timeRemaining.put(serverEvent, (long) (serverEvent.getFrequency() * serverEvent.getTimeUnit().getModifier()));
    }

    public static void setTimeRemaining(ServerEvent event, long remaining) {
        if (timeRemaining.contains(event)) {
            timeRemaining.remove(event);
            timeRemaining.put(event, remaining);
        }
    }

    public static long getTimeRemaining(ServerEvent event) {
        return timeRemaining.get(event);
    }

    private class ServerEventTask implements Runnable {
        public void run() {
            for (final ServerEvent serverEvent : timeRemaining.keySet()) {
                Long time = timeRemaining.get(serverEvent);
                time--;
                if(time % serverEvent.getTimeUnit().getModifier() == 0)
                    serverEvent.onTick(plugin, (int) (time / serverEvent.getTimeUnit().getModifier()));
                if (time <= 0) {
                    if ((Math.random()*100) <= serverEvent.getChance()) {
                        plugin.getServer().getScheduler().runTask(plugin, () -> {
                            serverEvent.onEvent(plugin);
                            Logger.log(plugin, "Event " + serverEvent.getName() + " ran.");
                        });
                    }
                    timeRemaining.put(serverEvent, (long) (serverEvent.getFrequency() * serverEvent.getTimeUnit().getModifier()));
                    continue;
                }
                timeRemaining.put(serverEvent, time);
            }
        }
    }
}
