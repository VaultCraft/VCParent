package net.vaultcraft.vcessentials.commands;

import com.google.common.collect.Lists;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Connor Hollasch on 9/26/14.
 */

public class VCHat extends ICommand {

    public VCHat(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        if (player.getItemInHand() == null || player.getItemInHand().getType().equals(Material.AIR)) {
            Form.at(player, Prefix.ERROR, "You must be holding an item to put on your head!");
            return;
        }

        if (player.getEquipment().getHelmet() != null) {
            if (player.getInventory().firstEmpty() == -1) {
                player.getWorld().dropItemNaturally(player.getEyeLocation(), player.getEquipment().getHelmet());
            }
            else {
                player.getInventory().addItem(player.getEquipment().getHelmet());
            }

            player.getEquipment().setHelmet(new ItemStack(Material.AIR));
        }

        Material mat = player.getItemInHand().getType();

        if (!(mat.isBlock())) {
            Form.at(player, Prefix.ERROR, "This item is not a block!");
            return;
        }

        player.getEquipment().setHelmet(player.getItemInHand());
        player.getInventory().removeItem(player.getItemInHand());
        player.updateInventory();
        Form.at(player, Prefix.SUCCESS, "Enjoy your net hat!");
    }
}
