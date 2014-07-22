package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.database.sql.MySQL;
import net.vaultcraft.vcutils.database.sql.Statements;
import net.vaultcraft.vcutils.logging.Logger;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.util.DateUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Nick on 7/21/2014.
 */
public class VCMute extends ICommand implements Listener {

    private HashMap<UUID, Date> mutes = new HashMap<>();
    private Plugin plugin;

    public VCMute(final Plugin plugin, String name, Group permission, String... aliases) {
        super(name, permission, aliases);
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                VCUtils.getInstance().mySQL.addQuery(Statements.QUERYALL.getSql("Mutes"), new MySQL.ISqlCallback() {
                    @Override
                    public void onSuccess(ResultSet rs) {
                        try {
                            while (rs.next()) {
                                if (!rs.getBoolean("Unmuted")) {
                                    UUID name = UUID.fromString(rs.getString("MutedID"));
                                    Date date = rs.getDate("Temp");
                                    mutes.put(name, date);
                                }
                            }
                        } catch (SQLException e) {
                            Logger.error(plugin, e);
                        }
                    }

                    @Override
                    public void onFailure(SQLException e) {
                        Logger.error(plugin, e);
                    }
                });
            }
        });
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Format: /mute <player> (Time:time) (reason)");
            return;
        }

        if (args.length == 1) {
            OfflinePlayer player1 = Bukkit.getPlayer(args[0]);
            if (player1 == null) {
                player1 = Bukkit.getOfflinePlayer(args[0]);
                if (player1 == null) {
                    Form.at(player, Prefix.ERROR, "Player: " + args[0] + " has never joined the server.");
                    return;
                }
            }
            if (mute(player1, player, null, null)) {
                Form.at(player, Prefix.SUCCESS, "Player: " + player1.getName() + " is muted.");
            } else {
                Form.at(player, Prefix.SUCCESS, "Player: " + player1.getName() + " is unmuted.");
            }
        }

        if (args.length > 1) {
            OfflinePlayer player1 = Bukkit.getPlayer(args[0]);
            if (player1 == null) {
                player1 = Bukkit.getOfflinePlayer(args[0]);
                if (player1 == null) {
                    Form.at(player, Prefix.ERROR, "Player: " + args[0] + " has never joined the server.");
                    return;
                }
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

            mute(player1, player, reason.toString(), temp);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (mutes.containsKey(e.getPlayer().getUniqueId())) {
            if (mutes.get(e.getPlayer().getUniqueId()) == null) {
                e.setCancelled(true);
            } else {
                Date now = new Date();
                if (now.after(mutes.get(e.getPlayer().getUniqueId()))) {
                    unmute(e.getPlayer());
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    private void unmute(OfflinePlayer player) {
        VCUtils.getInstance().mySQL.updateThread.add(Statements.UPDATE.getSql("Mutes", "Unmuted=1", "MutedID='" + player.getUniqueId().toString() + "' AND Unmuted=0"));
        mutes.remove(player.getUniqueId());
    }

    private boolean mute(OfflinePlayer mutted, Player mutter, String reason, Date temp) {
        if (mutes.containsKey(mutted.getUniqueId())) {
            unmute(mutted);
            return false;
        } else {
            mutes.put(mutted.getUniqueId(), temp);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            VCUtils.getInstance().mySQL.updateThread.add(Statements.INSERT.getSql("Mutes",
                    "'" + mutted.getUniqueId().toString() + "', '" +
                            mutted.getName() + "', '" +
                            mutter.getUniqueId().toString() + "', '" +
                            mutter.getName() + "', '" +
                            reason + "', " +
                            MySQL.getDate() + ", " +
                            sdf.format(temp) + ", 0"
            ));
            return true;
        }
    }
}
