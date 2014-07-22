package net.vaultcraft.vcutils;

import net.vaultcraft.vcutils.command.CommandManager;
import net.vaultcraft.vcutils.config.ClassConfig;
import net.vaultcraft.vcutils.database.sql.MySQL;
import net.vaultcraft.vcutils.database.sql.SQLInfo;
import net.vaultcraft.vcutils.database.sql.Statements;
import net.vaultcraft.vcutils.listener.CommonPlayerListener;
import net.vaultcraft.vcutils.listener.ProtectionListener;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

/**
 * Created by Connor on 7/19/14. Designed for the VCUtils project.
 */

public class VCUtils extends JavaPlugin {

    private static VCUtils instance;
    public MySQL mySQL;


    public void onEnable() {
        instance = this;

        ClassConfig.loadConfig(SQLInfo.class, getConfig());
        mySQL = new MySQL(this, SQLInfo.host, SQLInfo.port, SQLInfo.database_name, SQLInfo.username, SQLInfo.password);
        mySQL.updateThread.add(Statements.TABLE.getSql("Commands",
                "SenderID TINYTEXT NOT NULL," +
                        "SenderName CHAR(16) NOT NULL," +
                        "SenderGroup CHAR(30) NOT NULL," +
                        "Command TEXT NOT NULL," +
                        "Time DATETIME NOT NULL"
        ));
        mySQL.updateThread.add(Statements.TABLE.getSql("Log",
                "PluginName CHAR(64) NOT NULL," +
                        "PluginVersion CHAR(10) NOT NULL," +
                        "Message TEXT NOT NULL," +
                        "Time DATETIME NOT NULL"
        ));
        mySQL.updateThread.add(Statements.TABLE.getSql("Chat",
                "ChatterID TINYTEXT NOT NULL," +
                        "ChatterName CHAR(16) NOT NULL," +
                        "Message TEXT NOT NULL," +
                        "Time DATETIME NOT NULL"
        ));
        getServer().getPluginManager().registerEvents(new CommandManager(), this);
        initListeners();

        for (Player player : Bukkit.getOnlinePlayers()) {
            CommonPlayerListener.getInstance().onPlayerJoin(new PlayerJoinEvent(player, null));
        }
    }

    public void onDisable() {
        try {
            mySQL.close();
        } catch (SQLException e) {
            Logger.error(this, e);
        }
        ClassConfig.updateConfig(SQLInfo.class, getConfig());
        saveConfig();
    }

    public void initListeners() {
        Bukkit.getPluginManager().registerEvents(new CommonPlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new ProtectionListener(), this);
    }

    public static VCUtils getInstance() {
        return instance;
    }
}
