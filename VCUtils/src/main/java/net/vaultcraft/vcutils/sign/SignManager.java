package net.vaultcraft.vcutils.sign;

import net.vaultcraft.vcutils.VCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;

import java.lang.String;import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Connor on 8/8/14. Designed for the VCPrison project.
 */

public class SignManager {

    private static HashMap<String, List<Location>> signs = new HashMap<>();

    public static void addSign(Location sign, String meta) {
        if (signs.containsKey(meta)) {
            List<Location> all = signs.remove(meta);
            all.add(sign);
            signs.put(meta, all);
        } else {
            List<Location> l = new ArrayList<>();
            l.add(sign);
            signs.put(meta, l);
        }
    }

    public static List<Location> fromMeta(String meta) {
        return signs.get(meta);
    }

    public static void updateSigns(String meta, String... text) {
        updateSign(signs.get(meta), text);
    }

    public static boolean remove(Location location) {
        String key = "";
        for (String k : signs.keySet()) {
            List<Location> v = signs.get(k);
            if (v.contains(location))
                key = k;
        }

        if (key.equals(""))
            return false;

        signs.remove(key);
        return true;
    }

    public static boolean remove(String key) {
        return (signs.remove(key) != null);
    }

    public static void updateSign(List<Location> signs, String... text) {
        for (Location loc : signs) {
            if (!(loc.getBlock().getState() instanceof Sign))
                return;

            final Sign sign = (Sign)loc.getBlock().getState();
            if (sign == null)
                return;

            if (!loc.getChunk().isLoaded())
                loc.getChunk().load();

            int x = 0;
            for (String s : text) {
                sign.setLine(x, ChatColor.translateAlternateColorCodes('&', s));
                x++;
            }

            Runnable run = new Runnable() {
                public void run() {
                    sign.update(true);
                }
            };
            Bukkit.getScheduler().scheduleSyncDelayedTask(VCUtils.getInstance(), run, 10);
        }
    }

    public static HashMap<String, List<Location>> all() {
        return signs;
    }
}
