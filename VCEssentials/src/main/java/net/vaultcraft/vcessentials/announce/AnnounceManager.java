package net.vaultcraft.vcessentials.announce;

import net.vaultcraft.vcessentials.VCEssentials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by Connor on 9/1/14. Designed for the VCUtils project.
 */

public class AnnounceManager {

    private static final int TIME = 60;

    private static HashMap<Player, AnnounceTask> announcements = new HashMap<>();
    private static HashMap<AnnounceTask, Integer> task_ids = new HashMap<>();

    public static void subscribeTask(Player player) {
        AnnounceTask make = new AnnounceTask(player);
        make._muted = false;
        announcements.put(player, make);

        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(VCEssentials.getInstance(), make, 20*(TIME), 20*(TIME));
        task_ids.put(make, taskId);
    }

    public static void muteTask(Player player) {
        announcements.get(player)._muted = true;
    }

    public static void unmuteTask(Player player) {
        announcements.get(player)._muted = false;
    }

    public static void unsubscribeTask(Player player) {
        if (!announcements.containsKey(player))
            return;

        AnnounceTask task = announcements.remove(player);
        Bukkit.getScheduler().cancelTask(task_ids.remove(task));
    }

    public static AnnounceTask getTask(Player player) {
        return announcements.get(player);
    }
}
