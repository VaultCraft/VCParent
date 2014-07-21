package net.vaultcraft.vcessentials;

import net.vaultcraft.vcessentials.commands.*;
import net.vaultcraft.vcutils.command.CommandManager;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class VCEssentials extends JavaPlugin {

    private static VCEssentials instance;

    public void onEnable() {
        instance = this;

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
        CommandManager.addCommand(new VCTime("time", Group.MOD));
        CommandManager.addCommand(new VCWeather("weather", Group.MOD));
    }

    public void onDisable() {

    }
}
