package net.vaultcraft.vcutils.protection;

import net.vaultcraft.vcutils.user.Group;
import org.bukkit.event.Event;

import java.util.HashMap;

/**
 * Created by Connor on 7/21/14. Designed for the VCUtils project.
 */

public class ProtectedArea {

    private Area area;
    private HashMap<Class<? extends Event>, Boolean> protection = new HashMap<>();
    private Group membership = Group.DEVELOPER;

    public ProtectedArea(Area area) {
        this.area = area;
    }

    public void setGroupMembership(Group other) {
        this.membership = other;
    }

    public Group getMembership() {
        return membership;
    }

    public Area getArea() {
        return area;
    }

    public void addToProtection(Class<? extends Event> clazz, boolean cancel) {
        this.protection.put(clazz, cancel);
    }

    public boolean doCancel(Class<? extends Event> event) {
        return (protection.containsKey(event) ? protection.get(event) : false);
    }
}
