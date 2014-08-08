package net.vaultcraft.vcutils.sign;

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

    private static HashMap<String, List<Sign>> signs = new HashMap<>();

    public static void addSign(Sign sign, String meta) {
        if (signs.containsKey(meta)) {
            List<Sign> all = signs.remove(meta);
            all.add(sign);
            signs.put(meta, all);
        } else {
            List<Sign> l = new ArrayList<>();
            l.add(sign);
            signs.put(meta, l);
        }
    }

    public static List<Sign> fromMeta(String meta) {
        return signs.get(meta);
    }

    public static void updateSigns(String meta, String... text) {
        updateSign(signs.get(meta), text);
    }

    public static void updateSign(List<Sign> signs, String... text) {
        for (Sign sign : signs) {
            if (sign == null)
                return;

            Location loc = sign.getLocation();
            if (!loc.getChunk().isLoaded())
                loc.getChunk().load();

            int x = 0;
            for (String s : text) {
                sign.setLine(x, ChatColor.translateAlternateColorCodes('&', s));
                x++;
            }

            sign.update();
        }
    }

    public static HashMap<String, List<Sign>> all() {
        return signs;
    }
}
