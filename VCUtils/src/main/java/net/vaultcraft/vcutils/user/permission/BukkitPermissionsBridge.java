package net.vaultcraft.vcutils.user.permission;

import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.UserLoadedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;

public class BukkitPermissionsBridge implements Listener {

    private BukkitPermissionsFile permsFile;
    private HashMap<Player, PermissionAttachment> activePermissions;

    public BukkitPermissionsBridge(Plugin plugin)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        try {
            permsFile = new BukkitPermissionsFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        activePermissions = new HashMap<>();
    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onUserLoad(UserLoadedEvent event) throws Exception {
        Player p = event.getUser().getPlayer();
        PermissionAttachment attachment = p.addAttachment(VCUtils.getInstance());
        for(Group g : event.getUser().getGroup().getAllGroups()) {
            List<String> perms = permsFile.permissions.get(g.getName());
            if(perms == null)
            {
                throw new Exception("Group " + g.getName() + " permission object is null! Go bug CK to fix this.");
            }
            for(String perm : perms) {
                attachment.setPermission(perm, true);
            }
        }


        activePermissions.put(p, attachment);

    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(activePermissions.containsKey(event.getPlayer())) {
            activePermissions.remove(event.getPlayer());
        }
    }
}
