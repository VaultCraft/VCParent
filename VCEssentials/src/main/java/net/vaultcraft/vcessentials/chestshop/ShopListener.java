package net.vaultcraft.vcessentials.chestshop;

import net.vaultcraft.vcessentials.VCEssentials;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

/**
 * Created by tacticalsk8er on 10/29/2014.
 */
public class ShopListener implements Listener {

    public ShopListener() {
        Bukkit.getPluginManager().registerEvents(this, VCEssentials.getInstance());
    }
}
