package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.item.inventory.InventoryBuilder;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by Connor on 8/31/14. Designed for the VCUtils project.
 */

public class VCServer extends ICommand {

    public VCServer(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            //open GUI etc...
            InventoryBuilder build = InventoryBuilder.builder();
        }
    }

    private static class VCServerListener implements Listener {
        @EventHandler
        public void onGUIClick(InventoryClickEvent event) {

        }
    }
}
