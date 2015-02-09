package net.vaultcraft.vcutils.command;

import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */
public abstract class ICommand {

    private String name;
    private Permission permission;
    private String[] aliases;
    protected Map<String, String> subCmds = new HashMap<>();
    protected Map<String, Group> groupPerms = new HashMap<>();
    protected boolean display_group;

    public ICommand(String name, Permission permission, String... aliases) {
        this.name = name;
        this.permission = permission;
        this.aliases = aliases;
    }

    public ICommand(String name, Permission permission) {
        this(name, permission, new String[0]);
    }

    @Deprecated
    public ICommand(String name, Group permission, String... aliases)
    {
        this(name, new Permission("vc.legacy."+permission.getName()), aliases);
    }

    public boolean checkPerms(User player) {
        return player.getPlayer().hasPermission(permission);
    }

    public String getName() {
        return name;
    }

    public Permission getPermission() {
        return permission;
    }

    public String[] getAliases() {
        return aliases;
    }

    public abstract void processCommand(Player player, String[] args);

    public Map<String, String> getHelp() {
        return subCmds;
    }

    public Map<String, Group> getGroupPerms() { return groupPerms; }

    public boolean isDisplayingGroup() { return display_group; }
}
