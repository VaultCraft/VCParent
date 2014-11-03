package net.vaultcraft.vcessentials.auction;

import net.vaultcraft.vcessentials.VCEssentials;
import net.vaultcraft.vcessentials.auction.settings.Question;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Connor Hollasch
 * @since 11/2/2014
 */
public class AucListener implements Listener {

    public AucListener() {
        Bukkit.getPluginManager().registerEvents(this, VCEssentials.getInstance());
    }

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent event) {
        //Check for valid chat question
        if (Question.scanForQuestion(event))
            event.setCancelled(true);
    }
}
