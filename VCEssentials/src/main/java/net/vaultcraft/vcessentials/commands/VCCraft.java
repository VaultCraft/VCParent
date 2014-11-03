package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

/**
 * Created by tacticalsk8er on 11/2/2014.
 */
public class VCCraft extends ICommand {

    public VCCraft(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        player.openInventory(Bukkit.createInventory(null, InventoryType.WORKBENCH));
    }
}
