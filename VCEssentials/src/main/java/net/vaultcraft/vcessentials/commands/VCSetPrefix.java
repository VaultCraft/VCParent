package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.string.StringUtils;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.OfflineUser;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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

        if (args[0].equalsIgnoreCase("clear")) {
            Player find = Bukkit.getPlayer(args[1]);
            if (find == null) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                if(offlinePlayer != null) {
                    OfflineUser user = OfflineUser.getOfflineUser(offlinePlayer);
                    user.setPrefix(null);
                    Form.at(player, Prefix.SUCCESS, "You cleared " + offlinePlayer.getName() + "'s prefix!");
                }
                Form.at(player, Prefix.ERROR, "No such player! Format: /prefix [player] [prefix]");
                return;
            }

            User.fromPlayer(find).setPrefix(null);
            Form.at(player, Prefix.SUCCESS, "You cleared " + find.getName() + "'s prefix!");
            return;
        }

        String prefix = StringUtils.buildFromArray(args,1);

        Player find = Bukkit.getPlayer(args[0]);
        if (find == null) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
            if(offlinePlayer != null) {
                OfflineUser user = OfflineUser.getOfflineUser(offlinePlayer);
                user.setPrefix(prefix);
                Form.at(player, Prefix.SUCCESS, "You set " + offlinePlayer.getName() + "'s prefix to " + prefix);
            }
            Form.at(player, Prefix.ERROR, "No such player! Format: /prefix [player] [prefix]");
            return;
        }

        User.fromPlayer(find).setPrefix(prefix);
        Form.at(player, Prefix.SUCCESS, "You set " + find.getName() + "'s prefix to " + prefix);
    }
}
