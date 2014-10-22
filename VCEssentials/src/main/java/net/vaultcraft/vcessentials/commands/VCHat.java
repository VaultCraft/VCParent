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

    public static List<Player> hatPlayers = Lists.newArrayList();

    public VCHat(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        if(hatPlayers.contains(player)) {
            player.getEquipment().setHelmet(null);
            hatPlayers.remove(player);
            Form.at(player, Prefix.WARNING, "You have removed your hat.");
            return;
        }
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "You must specify an item to put on your head!");
            return;
        }

        Material mat = getMaterial(args[0]);

        if (mat == null) {
            Form.at(player, Prefix.ERROR, "No such material exists!");
            return;
        }

        if (!(mat.isBlock())) {
            Form.at(player, Prefix.ERROR, "This item is not a block!");
            return;
        }
        if(!hatPlayers.contains(player)) {
            if(player.getEquipment().getHelmet() != null) {
                Form.at(player, Prefix.ERROR, "Please remove your current helmet before getting a hat.");
                return;
            }
            player.getEquipment().setHelmet(new ItemStack(mat));
            hatPlayers.add(player);
            Form.at(player, Prefix.SUCCESS, "Enjoy your net hat!");
        }

    }

    private static Material getMaterial(String in) {
        in = in.toLowerCase();

        int id = -1;
        try { id = Integer.parseInt(in); } catch (NumberFormatException ex) {}

        if (id != -1)
            return Material.getMaterial(id);

        for (Material m : Material.values()) {
            if (m.toString().toLowerCase().replace("_", "").equals(in))
                return m;
        }

        return null;
    }
}
