package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.database.sql.MySQL;
import net.vaultcraft.vcutils.logging.Logger;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Nick on 7/21/2014.
 */
public class VCMute extends ICommand implements Listener {

    HashMap<String, Date> mutes = new HashMap<>();

    public VCMute(final Plugin plugin, String name, Group permission, String... aliases) {
        super(name, permission, aliases);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                VCUtils.getInstance().mySQL.addQuery("", new MySQL.ISqlCallback() {
                    @Override
                    public void onSuccess(ResultSet rs) {

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

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

    }
}
