package net.vaultcraft.vcutils.command;

import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.entity.Player;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */
public abstract class ICommand {

    private String name;
    private Group permission;
    private String[] aliases;

    public ICommand(String name, Group permission, String... aliases) {
        this.name = name;
        this.permission = permission;
        this.aliases = aliases;
    }

    public ICommand(String name, Group permission) {
        this(name, permission, new String[0]);
    }

    public boolean checkPerms(User player) {
        return player.getGroup().hasPermission(permission);
    }

    public String getName() {
        return name;
    }

    public Group gerPermission() {
        return permission;
    }

    public String[] getAliases() {
        return aliases;
    }

    public abstract void processCommand(Player player, String[] args);
}
