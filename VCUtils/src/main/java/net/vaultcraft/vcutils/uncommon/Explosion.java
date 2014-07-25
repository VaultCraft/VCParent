package net.vaultcraft.vcutils.uncommon;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;

/**
 * Created by Connor on 7/23/14. Designed for the VCUtils project.
 */

public class Explosion {

    public static List<Block> formExplosion(Location origin, double radius) {
        Location min = origin.clone().subtract(radius, radius, radius);
        Location max = origin.clone().add(radius, radius, radius);

        List<Block> list = Lists.newArrayList();
        for (int x = min.getBlockX(); x < max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y < max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z < max.getBlockZ(); z++) {
                    Location find = new Location(origin.getWorld(), x, y, z);
                    if (find.toVector().isInSphere(origin.toVector(), radius))
                        list.add(find.getBlock());
                }
            }
        }

        return list;
    }
}
