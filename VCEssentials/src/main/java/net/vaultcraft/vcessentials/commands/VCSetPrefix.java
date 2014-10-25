package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Connor Hollasch
 * @since 10/24/14
 */
public class VCSetPrefix extends ICommand {

    public VCSetPrefix(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        if (args.length < 1) {
            Form.at(player, Prefix.ERROR, "Format: /prefix [player] [prefix]");
            return;
        }

        Player find = Bukkit.getPlayer(args[0]);
        if (find == null) {
            Form.at(player, Prefix.ERROR, args[0] + " is not a valid player!");
            return;
        }

        String prefix = args[1];
        if (prefix.toLowerCase().equals("nothing") || prefix.toLowerCase().equals("null"))
            prefix = null;

        User.fromPlayer(find).setPrefix(prefix);
        Form.at(player, Prefix.SUCCESS, "You set " + find.getName() + "'s prefix to " + prefix);
    }
}
