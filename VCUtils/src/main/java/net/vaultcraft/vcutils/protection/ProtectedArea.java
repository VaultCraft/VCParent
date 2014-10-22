package net.vaultcraft.vcutils.protection;

import net.vaultcraft.vcutils.protection.flag.FlagType;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Event;

import java.util.HashMap;

/**
 * Created by Connor on 7/21/14. Designed for the VCUtils project.
 */

public class ProtectedArea {

    private Area area;
    private HashMap<FlagType, Boolean> protection = new HashMap<>();
    private Group membership = Group.DEVELOPER;
    private int priority = 0;

    public ProtectedArea(Area area) {
        this.area = area;
    }

    public void setGroupMembership(Group other) {
        membership = other;
    }

    public Group getMembership() {
        return membership;
    }

    public Area getArea() {
        return area;
    }

    public void addToProtection(FlagType type, boolean cancel) {
        if (protection.containsKey(type))
            protection.remove(type);
        
        protection.put(type, cancel);
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public boolean doCancel(Class<? extends Event> event) {
        return (protection.containsKey(event) ? protection.get(event) : false);
    }

    public HashMap<FlagType, Boolean> getProtection() {
        return protection;
    }
}
