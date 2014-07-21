package net.vaultcraft.vcutils.command;

import lombok.Getter;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.entity.Player;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */
public abstract class ICommand {

    @Getter
    private String name;
    @Getter
    private Group permission;
    @Getter
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

    public abstract void processCommand(Player player, String[] args);
}
