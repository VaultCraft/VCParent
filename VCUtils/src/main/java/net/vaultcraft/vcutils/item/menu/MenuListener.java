package net.vaultcraft.vcutils.item.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

/**
 * Created by Connor on 8/15/14. Designed for the VCUtils project.
 */

public class MenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ActiveMenu menu = MenuManager.fromPlayer((Player) event.getWhoClicked());

        if (menu == null)
            return;

        event.setCancelled(true);

        int slot = event.getSlot();
        MenuItem mi = menu.fromSlot(slot);
        if (mi == null)
            return;

        event.setCancelled(mi.onClick((Player)event.getWhoClicked()));
        menu.update();
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        ActiveMenu menu = MenuManager.fromPlayer((Player)event.getPlayer());

        if (menu == null)
            return;

        menu.getModel().onOpen((Player)event.getPlayer());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        ActiveMenu menu = MenuManager.fromPlayer((Player)event.getPlayer());

        if (menu == null)
            return;

        menu.getModel().onClose((Player)event.getPlayer());
    }
}
