package net.vaultcraft.vcessentials;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class VCEssentials extends JavaPlugin {

    private static VCEssentials instance;

    public void onEnable() {
        instance = this;
    }

    public static VCEssentials getInstance() {
        return instance;
    }

    private void initCommands() {

    }

    public void onDisable() {

    }
}
