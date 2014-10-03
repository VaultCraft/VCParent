package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Connor Hollasch on 9/26/14.
 */

public class VCHat extends ICommand {

    public VCHat(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "You must specify an item to put on your head!");
            return;
        }

        Material mat = Material.getMaterial(args[0]);
        if (mat == null) {
            Form.at(player, Prefix.ERROR, "No such material exists!");
            return;
        }

        if (!(mat.isBlock())) {
            Form.at(player, Prefix.ERROR, "This item is not a block!");
            return;
        }

        player.getEquipment().setHelmet(new ItemStack(mat));
        Form.at(player, Prefix.SUCCESS, "Enjoy your net hat!");
    }
}
