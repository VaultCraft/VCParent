package net.vaultcraft.vcutils;

import net.vaultcraft.vcutils.command.CommandManager;
import net.vaultcraft.vcutils.config.ClassConfig;
import net.vaultcraft.vcutils.database.sql.MySQL;
import net.vaultcraft.vcutils.database.sql.SQLInfo;
import net.vaultcraft.vcutils.listener.CommonPlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

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
        mySQL.updateThread.add("CREATE TABLE IF NOT EXISTS Commands(" +
                "ID INT NOT NULL AUTO_INCREMENT," +
                "PRIMARY KEY(ID)," +
                "SenderID TINYTEXT," +
                "SenderName CHAR(16)," +
                "SenderGroup CHAR(30)," +
                "Command TEXT," +
                "Time DATETIME)");
        mySQL.updateThread.add("CREATE TABLE IF NOT EXISTS Log(" +
                "ID INT NOT NULL AUTO_INCREMENT," +
                "PRIMARY KEY(ID)," +
                "PluginName CHAR(64)," +
                "PluginVersion CHAR(10)," +
                "Message TEXT," +
                "Time DATETIME)");
        getServer().getPluginManager().registerEvents(new CommandManager(), this);
        initListeners();

        for (Player player : Bukkit.getOnlinePlayers()) {
            CommonPlayerListener.getInstance().onPlayerJoin(new PlayerJoinEvent(player, null));
        }
    }

    public void onDisable() {
        ClassConfig.updateConfig(SQLInfo.class, getConfig());
    }

    public void initListeners() {
        Bukkit.getPluginManager().registerEvents(new CommonPlayerListener(), this);
    }

    public static VCUtils getInstance() {
        return instance;
    }
}
