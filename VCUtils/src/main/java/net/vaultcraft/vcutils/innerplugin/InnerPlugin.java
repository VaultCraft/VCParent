package net.vaultcraft.vcutils.innerplugin;

import org.bukkit.plugin.Plugin;

/**
 * @author Connor Hollasch
 * @since 10/12/14
 */
public abstract class InnerPlugin {

    /** ABSTRACT METHODS **/

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract Plugin getWrapperPlugin();

    /***********************/

    private static InnerPlugin instance;

    public InnerPlugin() {
        instance = this;
    }

    public static InnerPlugin getInstance() {
        return instance;
    }
}
