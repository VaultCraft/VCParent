package net.vaultcraft.vcessentials.listeners;

import net.vaultcraft.vcessentials.commands.VCHat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Sean on 10/18/2014.
 */
public class VCHatBugfixListener implements Listener {

    @EventHandler
    public void onInventoryPick(InventoryClickEvent event) {
        if(event.getSlotType() == InventoryType.SlotType.ARMOR && event.getWhoClicked() instanceof Player && VCHat.hatPlayers.contains(event.getWhoClicked())) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if(VCHat.hatPlayers.contains(event.getPlayer())) {
            event.getPlayer().getEquipment().setHelmet(null);
        }
    }
}
