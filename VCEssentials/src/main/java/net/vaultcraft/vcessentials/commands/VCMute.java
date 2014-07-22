package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.database.sql.MySQL;
import net.vaultcraft.vcutils.database.sql.Statements;
import net.vaultcraft.vcutils.logging.Logger;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import net.vaultcraft.vcutils.util.DateUtil;
import org.bukkit.Bukkit;
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
                Form.at(player, Prefix.ERROR, "Player: " + args[0] + " is not online.");
                return;
            }
            if (mute(User.fromPlayer(player1), player, null, null)) {
                Form.at(player, Prefix.SUCCESS, "Player: " + player1.getName() + " is muted.");
            } else {
                Form.at(player, Prefix.SUCCESS, "Player: " + player1.getName() + " is unmuted.");
            }
        }

        if (args.length > 1) {
            Player player1 = Bukkit.getPlayer(args[0]);
            if (player1 == null) {
                Form.at(player, Prefix.ERROR, "Player: " + args[0] + " is not online.");
                return;
            }

            StringBuilder reason = new StringBuilder();
            Date temp = null;

            if (args[1].contains("Time:")) {
                String time = args[1].replace("Time:", "");
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

            mute(User.fromPlayer(player1), player, reason.toString(), temp);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        User user = User.fromPlayer(e.getPlayer());

        if (user.isMuted()) {
            if (user.getTempMute() == null) {
                e.setCancelled(true);
            } else {
                Date now = new Date();
                if (now.after(user.getTempMute())) {
                    user.setMuted(false, null);
                } else {
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            VCUtils.getInstance().mySQL.updateThread.add(Statements.INSERT.getSql("Mutes",
                    "'" + muted.getPlayer().getUniqueId().toString() + "', '" +
                            muted.getPlayer().getName() + "', '" +
                            mutter.getUniqueId().toString() + "', '" +
                            mutter.getName() + "', '" +
                            reason + "', " +
                            MySQL.getDate() + ", " +
                            sdf.format(temp)
            ));
            return true;
        }
    }
}
