package net.vaultcraft.vcessentials;

import net.vaultcraft.vcessentials.commands.*;
import net.vaultcraft.vcessentials.file.ProtectionFile;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.command.CommandManager;
import net.vaultcraft.vcutils.database.sql.MySQL;
import net.vaultcraft.vcutils.database.sql.Statements;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.parser.ParseException;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class VCEssentials extends JavaPlugin {

    private static VCEssentials instance;
    private MySQL mySQL;

    public void onEnable() {
        instance = this;

        ProtectionFile.getInstance();
        initCommands();
        mySQL = VCUtils.getInstance().mySQL;
        mySQL.updateThread.add(Statements.TABLE.getSql("Bans",
                "BannedID TINYTEXT," +
                        "BannedName CHAR(16) NOT NULL," +
                        "BannerID TINYTEXT NOT NULL," +
                        "BannerName CHAR(16) NOT NULL," +
                        "Reason TEXT," +
                        "Time DATETIME NOT NULL," +
                        "Temp DATETIME,"
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
                        "Temp DATETIME,"
        ));
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
        CommandManager.addCommand(new VCKick("kick", Group.MOD));
        CommandManager.addCommand(new VCMute(this, "mute", Group.ADMIN));

        //protection
        CommandManager.addCommand(new VCProtection("protect", Group.DEVELOPER, "p", "region", "prot", "protection"));

        //redirects
        CommandManager.addRedirect("gms", "gamemode 0");
        CommandManager.addRedirect("gmc", "gamemode 1");
        CommandManager.addRedirect("gma", "gamemode 2");

        //whitelist
        CommandManager.addPluginWhitelist("/");
    }

    public void onDisable() {
        try {
            ProtectionFile.getInstance().saveAll();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
