package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.OfflineUser;
import net.vaultcraft.vcutils.user.User;
import net.vaultcraft.vcutils.util.DateUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Nicholas Peterson
 */
public class VCTempBan extends ICommand {

    public VCTempBan(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if(args.length < 2) {
            Form.at(player, Prefix.ERROR, "Format: /tempban <player> <time> (reason)");
            Form.at(player, Prefix.ERROR, "Time Format: y = year, mo = month, d = day, h = hour, m = minute, s = second. " +
                    "Example: 1h30m = 1 hour 30 minutes.");
        }

        if(args.length == 2) {
            Player player1 = Bukkit.getPlayer(args[0]);
            Date date;
            try {
                date = new Date(DateUtil.parseDateDiff(args[1], true));
            } catch (Exception e) {
                Form.at(player, Prefix.ERROR, "Time Format is invalid.");
                Form.at(player, Prefix.ERROR, "Time Format: y = year, mo = month, d = day, h = hour, m = minute, s = second." +
                        "Example: 1h30m = 1 hour 30 minutes.");
                return;
            }
            if (player1 == null) {
                //Offline
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                    OfflineUser user = OfflineUser.getOfflineUser(offlinePlayer);
                    user.setBanned(true, date);
                    Form.at(player, Prefix.SUCCESS, offlinePlayer.getName() + " has been banned!");
                    return;
                }
                Form.at(player, Prefix.ERROR, "Player does not exist.");
                return;
            }

            User user = User.fromPlayer(player1);
            user.setBanned(true, date);
            player1.kickPlayer("You have been banned until" + new SimpleDateFormat("y/M/d hh:mm:ss z").format(date) + "! You can post an appeal on our forums.");
            Form.at(player, Prefix.SUCCESS, player1.getName() + " has been banned!");
        }

        if(args.length > 2) {
            Player player1 = Bukkit.getPlayer(args[0]);
            Date date;
            try {
                date = new Date(DateUtil.parseDateDiff(args[1], true));
            } catch (Exception e) {
                Form.at(player, Prefix.ERROR, "Time Format is invalid.");
                Form.at(player, Prefix.ERROR, "Time Format: y = year, mo = month, d = day, h = hour, m = minute, s = second." +
                        "Example: 1h30m = 1 hour 30 minutes.");
                return;
            }
            if (player1 == null) {
                //Offline
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                    OfflineUser user = OfflineUser.getOfflineUser(offlinePlayer);
                    user.setBanned(true, date);
                    Form.at(player, Prefix.SUCCESS, offlinePlayer.getName() + " has been banned!");
                    return;
                }
                Form.at(player, Prefix.ERROR, "Player does not exist.");
                return;
            }

            User user = User.fromPlayer(player1);
            user.setBanned(true, date);
            StringBuilder sb = new StringBuilder();
            for(int i = 2; i < args.length; i++)
                sb.append(args[i]).append(" ");
            player1.kickPlayer("You have been banned until" + new SimpleDateFormat("y/M/d hh:mm:ss z").format(date) + "!" +
                    "Reason: " + sb.toString() + ". You can post an appeal on our forums.");
            Form.at(player, Prefix.SUCCESS, player1.getName() + " has been banned!");
        }
    }
}
