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
public class VCTempMute extends ICommand{


    public VCTempMute(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if(args.length < 2) {
            Form.at(player, Prefix.ERROR, "Format: /tempmute <player> <time> (reason)");
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
                    user.setMuted(true, date);
                    Form.at(player, Prefix.SUCCESS, offlinePlayer.getName() + " has been muted!");
                    return;
                }
                Form.at(player, Prefix.ERROR, "Player does not exist.");
                return;
            }

            User user = User.fromPlayer(player1);
            user.setMuted(true, date);
            Form.at(player1, Prefix.WARNING, "You have been muted until " + new SimpleDateFormat("y/M/d hh:mm:ss z").format(date) + ".");
            Form.at(player, Prefix.SUCCESS, player1.getName() + " has been muted!");
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
                    user.setMuted(true, date);
                    Form.at(player, Prefix.SUCCESS, offlinePlayer.getName() + " has been muted!");
                    return;
                }
                Form.at(player, Prefix.ERROR, "Player does not exist.");
                return;
            }

            User user = User.fromPlayer(player1);
            user.setMuted(true, date);
            StringBuilder sb = new StringBuilder();
            for(int i = 2; i < args.length; i++)
                sb.append(args[i]).append(" ");
            Form.at(player1, Prefix.WARNING, "You have been muted until " + new SimpleDateFormat("y/M/d hh:mm:ss z").format(date)
                    + ". Reason: " + sb.toString() + ".");
            Form.at(player, Prefix.SUCCESS, player1.getName() + " has been muted!");
        }
    }
}
