package net.vaultcraft.vcutils;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Connor on 7/19/14. Designed for the VCUtils project.
 */

public class VCUtils extends JavaPlugin {

    private static VCUtils instance;

    public void onEnable() {
        instance = this;
    }

    public void onDisable() {

    }

    public static VCUtils getInstance() {
        return instance;
    }
}
