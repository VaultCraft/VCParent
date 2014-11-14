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

        enabled = true;
    }

    public abstract void onDisable();

    public abstract Plugin getWrapperPlugin();

    /***********************/

    protected boolean enabled = false;
}
