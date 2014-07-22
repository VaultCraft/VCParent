package net.vaultcraft.vcessentials;

import net.vaultcraft.vcessentials.commands.*;
import net.vaultcraft.vcessentials.file.ProtectionFile;
import net.vaultcraft.vcutils.command.CommandManager;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class VCEssentials extends JavaPlugin {

    private static VCEssentials instance;

    public void onEnable() {
        instance = this;

        ProtectionFile.getInstance();
        initCommands();
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
        CommandManager.addCommand(new VCHeal("heal", Group.ADMIN, "h", "feed"));

        //protection
        CommandManager.addCommand(new VCProtection("protect", Group.DEVELOPER, "p", "region"));

        //redirects
        CommandManager.addRedirect("gms", "gamemode 0");
        CommandManager.addRedirect("gmc", "gamemode 1");
        CommandManager.addRedirect("gma", "gamemode 2");

        //whitelist
        CommandManager.addPluginWhitelist("/");
    }

    public void onDisable() {
        ProtectionFile.getInstance().saveAll();
    }
}
