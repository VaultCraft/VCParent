package net.vaultcraft.vcutils.protection;

import org.bukkit.Location;

/**
 * Created by Connor on 7/21/14. Designed for the VCUtils project.
 */

public class Area {

    private Location min;
    private Location max;

    public Area(Location min, Location max) {
        this.min = min;
        this.max = max;
    }

    public boolean isInArea(Location position) {
        if (min == null && max == null)
            return true;

        if (!(position.getWorld().equals(min.getWorld())))
            return false;

        double xMin = Math.min(min.getX(), max.getX());
        double yMin = Math.min(min.getY(), max.getY());
        double zMin = Math.min(min.getZ(), max.getZ());

        double xMax = Math.max(min.getX(), max.getX());
        double yMax = Math.max(min.getY(), max.getY());
        double zMax = Math.max(min.getZ(), max.getZ());

        double x = position.getX();
        double y = position.getY();
        double z = position.getZ();

        return (xMin <= x && x <= xMax && yMin <= y && y <= yMax && zMin <= z && z <= zMax);
    }

    public Location getMin() {
        return min;
    }

    public Location getMax() {
        return max;
    }

    public void setMin(Location min) {
        this.min = min;
    }

    public void setMax(Location max) {
        this.max = max;
    }
}
