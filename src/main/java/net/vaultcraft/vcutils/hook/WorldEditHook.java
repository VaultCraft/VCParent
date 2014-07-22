package net.vaultcraft.vcutils.hook;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import net.vaultcraft.vcutils.protection.Area;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Connor on 7/21/14. Designed for the VCUtils project.
 */

public class WorldEditHook {

    private static WorldEditHook instance;

    public static WorldEditHook getInstance() {
        return (instance == null ? (instance = new WorldEditHook()) : instance);
    }

    private WorldEditPlugin wep;

    public WorldEditHook() {
        wep = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
    }

    public Area getFromSelection(Player player) {
        Selection sel = wep.getSelection(player);
        if (sel == null)
            return null;

        Area area = new Area(sel.getMinimumPoint(), sel.getMaximumPoint());
        return area;
    }
}
