package net.vaultcraft.vcessentials.auction.settings;

import net.vaultcraft.vcessentials.VCEssentials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;

/**
 * @author Connor Hollasch
 * @since 11/2/2014
 */
public class Question {

    private static HashMap<Player, Runnable> questions = new HashMap<>();

    public static void registerQuestion(Player player, Runnable question) {
        questions.put(player, question);
    }

    public static boolean scanForQuestion(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String msg = event.getMessage();

        if (questions.containsKey(player)) {
            //Schedule SYNC runnable for question
            Bukkit.getScheduler().scheduleSyncDelayedTask(VCEssentials.getInstance(), questions.remove(player));
            return true;
        }

        return false;
    }
}
