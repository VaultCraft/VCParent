package net.vaultcraft.vcessentials.guild.commands;

import net.vaultcraft.vcessentials.guild.Role;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

/**
 * Created by tacticalsk8er on 11/21/2014.
 */
public abstract class VCSub {

    String name;
    Role minRole;
    Group minGroup;
    String[] aliases;

    public VCSub(String name, Role minRole, Group minGroup, String... aliases) {
        this.name = name;
        this.minRole = minRole;
        this.minGroup = minGroup;
        this.aliases = aliases;
    }

    public abstract void onCommand(Player player, String[] args);


    public String getName() {
        return name;
    }

    public Role getMinRole() {
        return minRole;
    }

    public Group getMinGroup() {
        return minGroup;
    }

    public String[] getAliases() {
        return aliases;
    }
}
