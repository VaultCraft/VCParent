package net.vaultcraft.vcutils.protection;

import org.bukkit.Location;

import java.util.HashMap;

/**
 * Created by Connor on 7/21/14. Designed for the VCUtils project.
 */

public class ProtectionManager {

    private HashMap<String, ProtectedArea> protect = new HashMap<>();

    public static ProtectionManager instance;

    public static ProtectionManager getInstance() {
        return (instance == null ? (instance = new ProtectionManager()) : instance);
    }

    public ProtectionManager() {
        protect.put("global", new ProtectedArea(new Area(null, null)));
    }

    public ProtectedArea getArea(String name) {
        return protect.get(name.toLowerCase());
    }

    public void addToProtection(String name, ProtectedArea area) {
        this.protect.put(name.toLowerCase(), area);
    }

    public void removeFromProtection(String name) {
        this.protect.remove(name);
    }

    public ProtectedArea fromLocation(Location loc) {
        for (ProtectedArea area : protect.values()) {
            if (area.getArea().isInArea(loc))
                return area;
        }
        return null;
    }

    public HashMap<String, ProtectedArea> getRegions() {
        return protect;
    }
}
