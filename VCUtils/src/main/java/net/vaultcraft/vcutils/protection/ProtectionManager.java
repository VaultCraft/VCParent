package net.vaultcraft.vcutils.protection;

import com.google.common.collect.Lists;
import net.vaultcraft.vcutils.protection.flag.FlagResult;
import net.vaultcraft.vcutils.protection.flag.FlagType;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Location;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

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
        ProtectedArea area = new ProtectedArea(new Area(null, null));
        area.setPriority(-1);
        protect.put("global", area);
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

    public HashMap<String, ProtectedArea> getRegions() {
        return protect;
    }

    public Collection<ProtectedArea> fromLocation(Location location) {
        Collection<ProtectedArea> coll = Lists.newArrayList();
        coll.addAll(protect.values().stream().filter(areas -> areas.getArea().isInArea(location)).collect(Collectors.toList()));
        return coll;
    }

    public FlagResult getState(FlagType flag, Location loc) {
        Collection<ProtectedArea> within = fromLocation(loc);
        boolean cancel = false;
        ProtectedArea highestPriority = null;
        for (ProtectedArea w : within) {
            if (!(w.getProtection().containsKey(flag)))
                continue;

            if (highestPriority == null) {
                highestPriority = w;
                cancel = highestPriority.getProtection().get(flag);
                continue;
            }

            if (highestPriority.getPriority() < w.getPriority())
                highestPriority = w;

            cancel = highestPriority.getProtection().get(flag);
        }

        return new FlagResult((highestPriority == null ? Group.COMMON : highestPriority.getMembership()), cancel);
    }
}
