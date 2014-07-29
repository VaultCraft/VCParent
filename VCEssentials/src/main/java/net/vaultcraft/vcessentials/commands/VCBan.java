package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcessentials.VCEssentials;
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
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tacticalsk8er on 7/22/2014.
 */
public class VCBan extends ICommand implements Listener {

    private Plugin plugin;

    public VCBan(Plugin plugin, String name, Group permission, String... aliases) {
        super(name, permission, aliases);
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Format: /ban <player> (Time:time) (reason)");
            return;
        }

        if (args.length == 1) {
            Player player1 = Bukkit.getPlayer(args[0]);
            if (player1 == null) {
                Form.at(player, Prefix.ERROR, "No such player");
                return;
            }
            if (ban(User.fromPlayer(player1), player, "", null)) {
                Form.at(player, Prefix.SUCCESS, "Player: " + player1.getName() + " is banned.");
                player1.kickPlayer("You have been banned!");
            } else {
                Form.at(player, Prefix.SUCCESS, "Player: " + player1.getName() + " is unbanned.");
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

            if (ban(User.fromPlayer(player1), player, reason.toString(), temp)) {
                Form.at(player, Prefix.SUCCESS, "Player: " + player1.getName() + " is banned.");
                player1.kickPlayer("You have been banned for: " + reason.toString());
            } else {
                Form.at(player, Prefix.SUCCESS, "Player: " + player1.getName() + " is unbanned.");
            }
        }
    }

    private boolean ban(User banned, Player banner, String reason, Date temp) {
        if (banned.isBanned()) {
            banned.setBanned(false, null);
            return false;
        } else {
            banned.setBanned(true, temp);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (temp == null) {
                VCEssentials.getInstance().getMySQL().updateThread.add(Statements.INSERT.getSql("Bans",
                        "'" + banned.getPlayer().getUniqueId().toString() + "', '" +
                                banned.getPlayer().getName() + "', '" +
                                banner.getUniqueId().toString() + "', '" +
                                banner.getName() + "', '" +
                                Statements.makeSqlSafe(reason) + "', '" +
                                MySQL.getDate() + "', NULL"
                ));
            } else {
                VCEssentials.getInstance().getMySQL().updateThread.add(Statements.INSERT.getSql("Bans",
                        "'" + banned.getPlayer().getUniqueId().toString() + "', '" +
                                banned.getPlayer().getName() + "', '" +
                                banner.getUniqueId().toString() + "', '" +
                                banner.getName() + "', '" +
                                Statements.makeSqlSafe(reason) + "', '" +
                                MySQL.getDate() + "', '" +
                                sdf.format(temp) + "'"
                ));
            }
            return true;
        }
    }
}
