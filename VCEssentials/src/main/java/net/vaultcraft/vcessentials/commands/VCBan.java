package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.OfflineUser;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Created by tacticalsk8er on 7/22/2014.
 */
public class VCBan extends ICommand {

    public VCBan(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Format: /ban <player> (reason)");
            return;
        }

        if (args.length == 1) {
            Player player1 = Bukkit.getPlayer(args[0]);

            if (player1 == null) {
                //Offline
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (offlinePlayer != null) {
                    OfflineUser user = OfflineUser.getOfflineUser(offlinePlayer);
                    user.setBanned(true, null);
                    Form.at(player, Prefix.SUCCESS, offlinePlayer.getName() + " has been banned!");
                    return;
                }
                Form.at(player, Prefix.ERROR, "Player does not exist.");
                return;
            }

            User user = User.fromPlayer(player1);
            user.setBanned(true, null);
            player1.kickPlayer("You have been banned! You can post an appeal on our forums.");
            Form.at(player, Prefix.SUCCESS, player1.getName() + " has been banned!");
        }

        if (args.length > 1) {
            Player player1 = Bukkit.getPlayer(args[0]);

            if (player1 == null) {
                //Offline
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (offlinePlayer != null) {
                    OfflineUser user = OfflineUser.getOfflineUser(offlinePlayer);
                    user.setBanned(true, null);
                    Form.at(player, Prefix.SUCCESS, offlinePlayer.getName() + " has been banned!");
                    return;
                }
                Form.at(player, Prefix.ERROR, "Player does not exist.");
                return;
            }

            User user = User.fromPlayer(player1);
            user.setBanned(true, null);
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++)
                sb.append(args[i]).append(" ");
            player1.kickPlayer("You have been banned! Reason: " + sb.toString() + ". You can post an appeal on our forums.");
            Form.at(player, Prefix.SUCCESS, player1.getName() + " has been banned!");
        }
    }
}
