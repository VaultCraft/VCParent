package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Connor Hollasch on 9/26/14.
 */

public class VCHat extends ICommand {

    public static List<Player> hatPlayers;

    public VCHat(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "You must specify an item to put on your head!");
            return;
        }

        Material mat = Material.getMaterial(args[0].toUpperCase());
        if (mat == null) {
            Form.at(player, Prefix.ERROR, "No such material exists!");
            return;
        }

        if (!(mat.isBlock())) {
            Form.at(player, Prefix.ERROR, "This item is not a block!");
            return;
        }
        if(!hatPlayers.contains(player)) {
            player.getEquipment().setHelmet(new ItemStack(mat));
            hatPlayers.add(player);
            Form.at(player, Prefix.SUCCESS, "Enjoy your net hat!");
        } else {
            player.getEquipment().setHelmet(null);
            hatPlayers.remove(player);
            Form.at(player, Prefix.WARNING, "You have removed your hat.");
        }

    }
}
