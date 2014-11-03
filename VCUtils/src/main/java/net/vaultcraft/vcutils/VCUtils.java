package net.vaultcraft.vcutils;

import net.vaultcraft.vcutils.bossbar.BarAPI;
import net.vaultcraft.vcutils.command.CommandManager;
import net.vaultcraft.vcutils.config.ClassConfig;
import net.vaultcraft.vcutils.database.mongo.MongoDB;
import net.vaultcraft.vcutils.database.mongo.MongoInfo;
import net.vaultcraft.vcutils.database.sql.MySQL;
import net.vaultcraft.vcutils.database.sql.SQLInfo;
import net.vaultcraft.vcutils.database.sqlite.SQLite;
import net.vaultcraft.vcutils.file.FileController;
import net.vaultcraft.vcutils.innerplugin.InnerPlugin;
import net.vaultcraft.vcutils.innerplugin.VCPluginManager;
import net.vaultcraft.vcutils.item.menu.MenuListener;
import net.vaultcraft.vcutils.listener.CommonPlayerListener;
import net.vaultcraft.vcutils.listener.ProtectionListener;
import net.vaultcraft.vcutils.logging.Logger;
import net.vaultcraft.vcutils.network.MessageClient;
import net.vaultcraft.vcutils.network.NetworkInfo;
import net.vaultcraft.vcutils.sign.SignLoader;
import net.vaultcraft.vcutils.uncommon.GhostFactory;
import net.vaultcraft.vcutils.user.User;
import net.vaultcraft.vcutils.util.BungeeUtil;
import net.vaultcraft.vcutils.util.SignGUI;
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

    @ClassConfig.Config(path = "MongoDBName")
    public static String mongoDBName = "VaultCraft";
    @ClassConfig.Config(path = "ServerName")
    public static String serverName = "Lobby";
    @ClassConfig.Config(path = "UniqueServerName")
    public static String uniqueServerName = "Lobby1";

    private BarAPI barAPI;
    private static SignGUI signGUI;
    private static GhostFactory factory; public static GhostFactory getGhostFactory() { return factory; }

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

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeUtil());

        barAPI = new BarAPI();
        signGUI = new SignGUI(this);
        barAPI.onEnable();
        factory = new GhostFactory(this);

        try {
            new MessageClient(NetworkInfo.host, NetworkInfo.port).init();
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getServer().shutdown();
        }

        try {
            mongoDB = new MongoDB(MongoInfo.host, MongoInfo.port);
        } catch (UnknownHostException e) {
            Logger.error(this, e);
        }

        sqlite = new SQLite(this, this.getDataFolder()+"/sqlite.db"); // TODO un-hardcode this.

        mySQL = new MySQL(this, SQLInfo.host, SQLInfo.port, SQLInfo.database_name, SQLInfo.username, SQLInfo.password);
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

        barAPI.onDisable();

        for (InnerPlugin plugin : VCPluginManager.getPlugins().values()) {
            plugin.onDisable();
        }

        User.disable();
        mongoDB.close();

        SignLoader.getInstance().save();
    }

    public static SignGUI getSignGUI() {
        return signGUI;
    }

    public BarAPI getBarAPI() {
        return barAPI;
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
}
