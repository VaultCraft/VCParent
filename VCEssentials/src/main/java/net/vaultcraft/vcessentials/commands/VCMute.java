package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.logging.Logger;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import net.vaultcraft.vcutils.util.DateUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.Date;

/**
 * Created by Nick on 7/21/2014.
 */
public class VCMute extends ICommand implements Listener {

    private Plugin plugin;

    public VCMute(final Plugin plugin, String name, Group permission, String... aliases) {
        super(name, permission, aliases);
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Format: /mute <player> (Time:time) (reason)");
            return;
        }

        if (args.length == 1) {
            Player player1 = Bukkit.getPlayer(args[0]);
            if (player1 == null) {
                Form.at(player, Prefix.ERROR, "No such player");
                return;
            }
            if (mute(User.fromPlayer(player1), player, "", null)) {
                Form.at(player, Prefix.SUCCESS, "Player: " + player1.getName() + " is muted.");
                Form.at(player1, Prefix.WARNING, "&7You have been muted.");
            } else {
                Form.at(player, Prefix.SUCCESS, "Player: " + player1.getName() + " is no longer muted.");
                Form.at(player1, Prefix.WARNING, "&7You are no longer muted.");
            }
        }

        if (args.length > 1) {
            Player player1 = Bukkit.getPlayer(args[0]);
            if (player1 == null) {
                Form.at(player, Prefix.ERROR, "No such player");
                return;
            }

            StringBuilder reason = new StringBuilder();
            Date temp = null;

            if (args[1].contains("Time:") || args[1].contains("time:") || args[1].contains("T:") || args[1].contains("t:")) {
                String time = args[1].replace("Time:", "").replace("time:", "").replace("T:", "").replace("t:", "");
                try {
                    temp = new Date(DateUtil.parseDateDiff(time, true));
                } catch (Exception e) {
                    Logger.error(plugin, e);
                }
            } else {
                reason.append(args[1]);
                if (args.length - 1 != 2)
                    reason.append(" ");
            }

            for (int i = 2; i < args.length; i++) {
                reason.append(args[i]);
                if (i != args.length - 1)
                    reason.append(" ");
            }

            if (mute(User.fromPlayer(player1), player, reason.toString(), temp)) {
                Form.at(player, Prefix.SUCCESS, "Player: " + player1.getName() + " is muted.");
            } else {
                Form.at(player, Prefix.SUCCESS, "Player: " + player1.getName() + " is unmuted.");
            }
        }
    }

    private String _(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
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
                    Form.at(e.getPlayer(), Prefix.ERROR, "You are muted!");
                    e.setCancelled(true);
                }
            }
        }
    }

    private boolean mute(User muted, Player mutter, String reason, Date temp) {
        if (muted.isMuted()) {
            muted.setMuted(false, null);
            return false;
        } else {
            muted.setMuted(true, temp);
            return true;
        }
    }
}
