package net.vaultcraft.vcessentials;

import net.vaultcraft.vcessentials.commands.VCPromote;
import net.vaultcraft.vcutils.command.CommandManager;
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
        CommandManager.addCommand(new VCPromote());
    }

    public void onDisable() {

    }
}
