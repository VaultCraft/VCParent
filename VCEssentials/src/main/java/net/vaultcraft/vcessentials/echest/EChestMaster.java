package net.vaultcraft.vcessentials.echest;

import net.vaultcraft.vcessentials.VCEssentials;
import net.vaultcraft.vcutils.item.inventory.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;

/**
 * Created by Connor on 7/23/14. Designed for the VCUtils project.
 */

public class EChestMaster implements Listener {

    private HashMap<Player, Menu> activeEchests = new HashMap<>();

    public EChestMaster() {
        Bukkit.getPluginManager().registerEvents(this, VCEssentials.getInstance());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {
            //open
        }
    }
}