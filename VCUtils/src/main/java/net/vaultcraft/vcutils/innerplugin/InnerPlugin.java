package net.vaultcraft.vcutils.innerplugin;

import org.bukkit.plugin.Plugin;

/**
 * @author Connor Hollasch
 * @since 10/12/14
 */
public abstract class InnerPlugin {

    /** ABSTRACT METHODS **/

    public void onEnable() {
        if (enabled)
            return;
    }

    public abstract void onDisable();

    public abstract Plugin getWrapperPlugin();

    /***********************/

    private static InnerPlugin instance;

    private static boolean enabled = false;

    public InnerPlugin() {
        instance = this;
    }

    public static InnerPlugin getInstance() {
        return instance;
    }
}
