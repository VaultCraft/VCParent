package net.vaultcraft.vcessentials;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.vaultcraft.vcessentials.announce.AnnounceManager;
import net.vaultcraft.vcessentials.blocks.BEnderChest;
import net.vaultcraft.vcessentials.commands.*;
import net.vaultcraft.vcessentials.file.ProtectionFile;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.command.CommandManager;
import net.vaultcraft.vcutils.database.sql.MySQL;
import net.vaultcraft.vcutils.database.sql.Statements;
import net.vaultcraft.vcutils.database.sqlite.SQLite;
import net.vaultcraft.vcutils.logging.Logger;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import net.vaultcraft.vcutils.user.WhitelistManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class VCEssentials extends JavaPlugin implements Listener {

    private static VCEssentials instance;
    private MySQL mySQL;
    private SQLite sqlite;

    public void onEnable() {
        instance = this;

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getPluginManager().registerEvents(this, this);

        ProtectionFile.getInstance().load();
        initCommands();
        mySQL = VCUtils.getInstance().getMySQL();
        mySQL.updateThread.add(Statements.TABLE.getSql("Bans",
                "BannedID TINYTEXT," +
                        "BannedName CHAR(16) NOT NULL," +
                        "BannerID TINYTEXT NOT NULL," +
                        "BannerName CHAR(16) NOT NULL," +
                        "Reason TEXT," +
                        "Time DATETIME NOT NULL," +
                        "Temp DATETIME"
        ));
        mySQL.updateThread.add(Statements.TABLE.getSql("Kicks",
                "KickedID TINYTEXT NOT NULL," +
                        "KickedName CHAR(16) NOT NULL," +
                        "KickerID TINYTEXT NOT NULL," +
                        "KickerName CHAR(16) NOT NULL," +
                        "Reason TEXT," +
                        "Time DATETIME NOT NULL"
        ));
        mySQL.updateThread.add(Statements.TABLE.getSql("Mutes",
                "MutedID TINYTEXT NOT NULL," +
                        "MutedName CHAR(16)," +
                        "MuterID TINYTEXT NOT NULL," +
                        "MuterName CHAR(16) NOT NULL," +
                        "Reason TEXT," +
                        "Time DATETIME NOT NULL," +
                        "Temp DATETIME"
        ));

        sqlite = VCUtils.getInstance().getSqlite();
        sqlite.updateThread.add(Statements.TABLE_SQLITE.getSql("UserData",
                "User TEXT, " +
                "UserJSON TEXT"));

        Bukkit.getPluginManager().registerEvents(new BEnderChest(), this);

        saveDefaultConfig();

        WhitelistManager.setWhitelist(getConfig().getBoolean("whitelist-status"));
        for (String key : getConfig().getStringList("whitelist")) {
            WhitelistManager.addPlayer(key);
        }
    }

    public static VCEssentials getInstance() {
        return instance;
    }

    private void initCommands() {
        CommandManager.addCommand(new VCPromote("promote", Group.ADMIN, "demote", "setgroup"));
        CommandManager.addCommand(new VCMessage("message", Group.COMMON, "msg", "tell", "whisper"));
        CommandManager.addCommand(new VCReply("reply", Group.COMMON, "r", "respond"));
        CommandManager.addCommand(new VCTeleport("tp", Group.MOD, "teleport"));
        CommandManager.addCommand(new VCGamemode("gm", Group.ADMIN, "gamemode"));
        CommandManager.addCommand(new VCFly("fly", Group.MOD));
        CommandManager.addCommand(new VCGive("give", Group.ADMIN, "g"));
        CommandManager.addCommand(new VCTime("time", Group.MOD));
        CommandManager.addCommand(new VCWeather("weather", Group.MOD));
        CommandManager.addCommand(new VCHeal("heal", Group.ADMIN, "h"));
        CommandManager.addCommand(new VCFeed("feed", Group.ADMIN));
        CommandManager.addCommand(new VCGod(this, "god", Group.ADMIN, "godmode"));
        CommandManager.addCommand(new VCKick("kick", Group.HELPER));
        CommandManager.addCommand(new VCMute(this, "mute", Group.HELPER));
        CommandManager.addCommand(new VCBan(this, "ban", Group.MOD));
        CommandManager.addCommand(new VCMoney("money", Group.COMMON, "bal", "balance"));
        CommandManager.addCommand(new VCToken("tokens", Group.COMMON));
        CommandManager.addCommand(new VCTeleportHere("tphere", Group.MOD, "teleporthere"));
        CommandManager.addCommand(new VCSpeed("speed", Group.ADMIN));
        CommandManager.addCommand(new VCButcher("butcher", Group.ADMIN, "killall"));
        CommandManager.addCommand(new VCAddSign("addsign", Group.DEVELOPER, "createsign", "linksign"));
        CommandManager.addCommand(new VCRemoveSign("removesign", Group.DEVELOPER, "deletesign", "unlinksign"));
        CommandManager.addCommand(new VCWorld("world", Group.ADMIN));
        CommandManager.addCommand(new VCServer("server", Group.COMMON));
        CommandManager.addCommand(new VCHub("hub", Group.COMMON, "lobby", "cloud"));
        CommandManager.addCommand(new VCAnnounceToggle("announce", Group.COMMON, "announcements"));
        CommandManager.addCommand(new VCWhitelist("whitelist", Group.MANAGER, "wlist"));
        CommandManager.addCommand(new VCHat("hat", Group.SLIME));
        //protection
        CommandManager.addCommand(new VCProtection("protect", Group.DEVELOPER, "p", "region", "prot", "protection"));

        //redirects
        CommandManager.addRedirect("gms", "gamemode 0");
        CommandManager.addRedirect("gmc", "gamemode 1");
        CommandManager.addRedirect("gma", "gamemode 2");

        //whitelist
        CommandManager.addPluginWhitelist("/");
        CommandManager.addPluginWhitelist("reload");
        CommandManager.addPluginWhitelist("rl");
        CommandManager.addPluginWhitelist("npc");
        CommandManager.addPluginWhitelist("buy");
        CommandManager.addPluginWhitelist("buycraft");

        //LOL
        CommandManager.addCommand(new VCSpamFunStuff("spam", Group.OWNER, "funny", "bots"));
    }

    public void onDisable() {
        ProtectionFile.getInstance().save();

        getConfig().set("whitelist", WhitelistManager.getOnList());
        getConfig().set("whitelist-status", WhitelistManager.isWhiteListed());

        saveDefaultConfig();
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public SQLite getSqlite() { return sqlite; }

    public void sendPlayerToServer(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);
        player.sendPluginMessage(VCEssentials.getInstance(), "BungeeCord", out.toByteArray());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //console commands
        switch (cmd.getName().toLowerCase()) {
            case "promote": {
                if (args.length < 2) {
                    Logger.log(this, "Format: /setgroup <user> <rank>");
                    return true;
                }

                Player wrapped = Bukkit.getPlayer(args[0]);
                if (wrapped == null) {
                    Logger.log(this, "No such player! Format: /setgroup <user> <rank>");
                    return true;
                }

                Group select = Group.fromString(args[1]);
                if (select == null) {
                    Logger.log(this, "No such group! Format: /setgroup <user> <rank>");
                    return true;
                }

                User.fromPlayer(wrapped).setGroup(select);
                Logger.log(this, wrapped.getName() + " promoted to " + select.getName());
                break;
            }
            case "redir": {
                if (!(sender instanceof Player))
                    return true;

                String send = "";
                for (String arg : args) {
                    send+=arg+" ";
                }
                System.out.println("Redirecting command to chat="+send);
                ((Player)sender).chat("/"+send);
                return true;
            }
        }
        return true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        AnnounceManager.subscribeTask(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        AnnounceManager.unsubscribeTask(event.getPlayer());
    }
}
