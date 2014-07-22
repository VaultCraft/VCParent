package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.item.ItemParser;
import net.vaultcraft.vcutils.string.StringUtils;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class VCGive extends ICommand {

    public VCGive(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    /*
     * /give <user> <id> (data:<data>) (amount:<amount>) (name:<name>) (lore:<l1,l2,l3>) (enchantments:<e1@a1,e2@a2>)
     */
    public void processCommand(Player player, String[] args) {
        if (args.length < 2) {
            Form.at(player, Prefix.ERROR, "Format: /give <user> <id> (meta:value)");
            return;
        }

        Player find = Bukkit.getPlayer(args[0]);
        if (find == null) {
            Form.at(player, Prefix.ERROR, "No such player!");
            return;
        }

        if (find.getInventory().firstEmpty() == -1) {
            Form.at(player, Prefix.ERROR, "&e" + find.getName() + Prefix.ERROR.getChatColor() + " has no room in his/her inventory!");
            return;
        }

        int id = 1;
        try {
            id = Integer.parseInt(args[1]);
        } catch (NumberFormatException err) {
            Form.at(player, Prefix.ERROR, args[1] + " is not a valid number!");
            return;
        }

        ItemStack build = null;
        try {
            String parse = "id:" + id + " " + (args.length >= 3 ? StringUtils.buildFromArray(args, 2) : "");
            build = ItemParser.fromString(parse);
        } catch (Exception ex) {
            Form.at(player, Prefix.ERROR, "Could not parse item stack!");
            return;
        }

        find.getInventory().addItem(build);
        if (!find.equals(player))
            Form.at(find, "&e" + player.getName() + Prefix.VAULT_CRAFT.getChatColor() + " gave you a(n) &e" + build.getType().toString().toLowerCase() + Prefix.VAULT_CRAFT.getChatColor() + "!");
        Form.at(player, "You gave &e" + find.getName() + Prefix.VAULT_CRAFT.getChatColor() + " a(n) &e" + build.getType().toString().toLowerCase() + Prefix.VAULT_CRAFT.getChatColor() + "!");
    }
}
