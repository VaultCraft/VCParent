package net.vaultcraft.vcutils;

import net.vaultcraft.vcutils.command.CommandManager;
import net.vaultcraft.vcutils.config.ClassConfig;
import net.vaultcraft.vcutils.database.mongo.MongoDB;
import net.vaultcraft.vcutils.database.mongo.MongoInfo;
import net.vaultcraft.vcutils.database.sql.MySQL;
import net.vaultcraft.vcutils.database.sql.SQLInfo;
import net.vaultcraft.vcutils.database.sql.Statements;
import net.vaultcraft.vcutils.database.sqlite.SQLite;
import net.vaultcraft.vcutils.file.FileController;
import net.vaultcraft.vcutils.item.menu.MenuListener;
import net.vaultcraft.vcutils.listener.CommonPlayerListener;
import net.vaultcraft.vcutils.listener.ProtectionListener;
import net.vaultcraft.vcutils.logging.Logger;
import net.vaultcraft.vcutils.network.Client;
import net.vaultcraft.vcutils.network.NetworkInfo;
import net.vaultcraft.vcutils.sign.SignLoader;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.UnknownHostException;
import java.sql.SQLException;

/**
 * Created by Connor on 7/19/14. Designed for the VCUtils project.
 */

public class VCUtils extends JavaPlugin {

    private static VCUtils instance;
    private MySQL mySQL;
    private MongoDB mongoDB;
    private SQLite sqlite;
    private Client client;

    @ClassConfig.Config(path = "ServerName")
    public static String serverName = "Lobby";
    @ClassConfig.Config(path = "UniqueServerName")
    public static String uniqueServerName = "Lobby1";

    public void onEnable() {
        instance = this;

        ClassConfig.loadConfig(SQLInfo.class, getConfig());
        ClassConfig.loadConfig(MongoInfo.class, getConfig());
        ClassConfig.loadConfig(NetworkInfo.class, getConfig());
        ClassConfig.loadConfig(VCUtils.class, getConfig());
        ClassConfig.updateConfig(SQLInfo.class, getConfig());
        ClassConfig.updateConfig(MongoInfo.class, getConfig());
        ClassConfig.updateConfig(NetworkInfo.class, getConfig());
        ClassConfig.updateConfig(VCUtils.class, getConfig());
        saveConfig();

        client = new Client(NetworkInfo.host, NetworkInfo.port);

        try {
            mongoDB = new MongoDB(MongoInfo.host, MongoInfo.port);
        } catch (UnknownHostException e) {
            Logger.error(this, e);
        }

        sqlite = new SQLite(this, this.getDataFolder()+"/sqlite.db"); // TODO un-hardcode this.

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

        FileController fc = new SignLoader();
        fc.load();

        MenuListener ml = new MenuListener();
        Bukkit.getPluginManager().registerEvents(ml, this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            CommonPlayerListener.getInstance().onPlayerJoin(new PlayerJoinEvent(player, null));
        }
    }

    public void onDisable() {
        try {
            mySQL.close();
            sqlite.close();
        } catch (SQLException e) {
            Logger.error(this, e);
        }

        User.disable();
        mongoDB.close();
        client.stop();

        SignLoader.getInstance().save();
    }

    public void initListeners() {
        Bukkit.getPluginManager().registerEvents(new CommonPlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new ProtectionListener(), this);
    }

    public static VCUtils getInstance() {
        return instance;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public MongoDB getMongoDB() {
        return mongoDB;
    }

    public SQLite getSqlite() { return sqlite;}

    public Client getClient() {
        return client;
    }
}
