package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.logging.Logger;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.OfflineUser;
import net.vaultcraft.vcutils.user.User;
import net.vaultcraft.vcutils.util.DateUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nick on 7/21/2014.
 */
public class VCMute extends ICommand implements Listener {


    public VCMute(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
        Bukkit.getPluginManager().registerEvents(this, VCUtils.getInstance());
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Format: /mute <player> (reason)");
            return;
        }

        if(args.length == 1) {
            Player player1 = Bukkit.getPlayer(args[0]);

            if (player1 == null) {
                //Offline
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (offlinePlayer != null) {
                    OfflineUser user = OfflineUser.getOfflineUser(offlinePlayer);
                    user.setMuted(true, null);
                    Form.at(player, Prefix.SUCCESS, offlinePlayer.getName() + " has been muted!");
                    return;
                }
                Form.at(player, Prefix.ERROR, "Player does not exist.");
                return;
            }

            User user = User.fromPlayer(player1);
            user.setMuted(true, null);
            Form.at(player1, Prefix.WARNING, "You have been muted!");
            Form.at(player, Prefix.SUCCESS, player1.getName() + " has been muted!");
        }

        if(args.length > 1) {
            Player player1 = Bukkit.getPlayer(args[0]);

            if (player1 == null) {
                //Offline
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (offlinePlayer != null) {
                    OfflineUser user = OfflineUser.getOfflineUser(offlinePlayer);
                    user.setMuted(true, null);
                    Form.at(player, Prefix.SUCCESS, offlinePlayer.getName() + " has been muted!");
                    return;
                }
                Form.at(player, Prefix.ERROR, "Player does not exist.");
                return;
            }

            User user = User.fromPlayer(player1);
            user.setMuted(true, null);
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++)
                sb.append(args[i]).append(" ");
            Form.at(player1, Prefix.WARNING, "You have been Muted! Reason: " + sb.toString() + ".");
            Form.at(player, Prefix.SUCCESS, player1.getName() + " has been muted!");
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        User user = User.fromPlayer(e.getPlayer());

        if (user.isMuted()) {
            if (user.getTempMute() == null) {
                Form.at(e.getPlayer(), Prefix.ERROR, "You are muted!");
                e.setCancelled(true);
            } else {
                Date now = new Date();
                if (now.after(user.getTempMute())) {
                    user.setMuted(false, null);
                } else {
                    Form.at(e.getPlayer(), Prefix.ERROR, "You are muted until" + new SimpleDateFormat("MM/dd/yy hh:mm:ss z").format(user.getTempMute()) + "!");
                    e.setCancelled(true);
                }
            }
        }
    }
}
