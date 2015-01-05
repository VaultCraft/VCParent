package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import net.vaultcraft.vcutils.user.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Connor Hollasch
 * @since 12/26/2014
 */
public class VCPermission extends ICommand {

    public VCPermission(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        if (args.length < 3) {
            Form.at(player, Prefix.ERROR, "Format: /permission <add|remove|list> (user) (rank)");
            return;
        }

        Player find = Bukkit.getPlayer(args[1]);
        if (find == null) {
            Form.at(player, Prefix.ERROR, "Could not find player &e" + args[0]);
            return;
        }

        User user = User.fromPlayer(find);

        Permission perm = Permission.valueOf(args[2].toUpperCase());
        if (perm == null) {
            Form.at(player, Prefix.ERROR, "Could not find permission &e" + args[1]);
            return;
        }

        if (args[0].equalsIgnoreCase("list")) {
            Form.at(player, Prefix.VAULT_CRAFT, "Permissions...");
            for (Permission p : Permission.values()) {
                Form.at(player, Prefix.VAULT_CRAFT, "&e- &7" + p.toString());
            }
            return;
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (!(user.getGroup().getPermissions().contains(perm))) {
                Form.at(player, Prefix.ERROR, "&e" + find.getName() + Prefix.ERROR.getChatColor() + " does not have this permission!");
                return;
            }

            user.getGroup().removePermission(perm);
            Form.at(player, Prefix.SUCCESS, "Permission &e" + perm.toString().replace("_", " ") + Prefix.SUCCESS.getChatColor() + " removed from &e" + find.getName());
            return;
        } else if (args[0].equalsIgnoreCase("add")) {
            if (user.getGroup().getPermissions().contains(perm)) {
                Form.at(player, Prefix.ERROR, "&e" + find.getName() + Prefix.ERROR.getChatColor() + " already has this permission!");
                return;
            }

            user.getGroup().addPermission(perm);
            Form.at(player, Prefix.SUCCESS, "Permission &e" + perm.toString().replace("_", " ") + Prefix.SUCCESS.getChatColor() + " added to &e" + find.getName());
        } else {
            Form.at(player, Prefix.ERROR, "Format: /permission <add|remove|list> (user) (rank)");
            return;
        }
    }
}
