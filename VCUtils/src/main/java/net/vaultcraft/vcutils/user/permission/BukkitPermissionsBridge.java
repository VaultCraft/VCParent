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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
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

    public class BukkitPermissionsFile {

        private HashMap<String, List<String>> permissions;
        File permsJson;

        private BukkitPermissionsFile() throws Exception {
            permissions = new HashMap<>();
            permsJson = new File(VCUtils.getInstance().getDataFolder().getAbsolutePath(), "permissions.json");
            if(!permsJson.exists())
            {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    permsJson.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                GenDefaultPermissions(new JSONObject());
            } else {


                JSONParser parser = new JSONParser();
                JSONObject data = null;

                data = (JSONObject) parser.parse(new FileReader(permsJson));

                for (Group g : Group.values()) {
                    permissions.put(g.getName(), (List<String>) data.get(g.getName()));
                }
            }

        }


        private void GenDefaultPermissions(JSONObject data) throws IOException {
            for(Group g : Group.values())
            {
                data.put(g.getName(), Arrays.asList("vc.legacy."+g.getName()));
            }

            FileWriter writer = new FileWriter(permsJson, false);
            writer.write(data.toJSONString());
            writer.flush();
            writer.close();
        }
    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onUserLoad(UserLoadedEvent event) throws Exception {
        Player p = event.getUser().getPlayer();
        PermissionAttachment attachment = p.addAttachment(VCUtils.getInstance());
        for(Group g : event.getUser().getGroup().getAllGroups()) {
            List<String> perms = permsFile.permissions.get(g.toString());
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
