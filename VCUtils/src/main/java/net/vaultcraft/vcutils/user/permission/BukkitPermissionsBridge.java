package net.vaultcraft.vcutils.user.permission;

import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.event.Listener;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class BukkitPermissionsBridge implements Listener {
    private class BukkitPermissionsFile {

        private HashMap<Group, List<String>> permissions;
        File permsJson;

        private BukkitPermissionsFile() throws Exception {
            permissions = new HashMap<>();
            permsJson = new File(VCUtils.getInstance().getDataFolder().getAbsolutePath(), "permissions.json");
            boolean success = false;
            if(!permsJson.exists())
            {
                try {
                    success = permsJson.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(!success)
            {
                throw new Exception("Couldn't create new permissions JSON file.");
            }

            JSONParser parser = new JSONParser();
            JSONObject data = null;

            data = (JSONObject) parser.parse(new FileReader(permsJson));
            if(data.isEmpty())
            {
                GenDefaultPermissions(data);
            }

            for(Group g : Group.values())
            {
                permissions.put(g, (List<String>) data.get(g.getName()));
            }


        }


        private void GenDefaultPermissions(JSONObject data) throws IOException {
            for(Group g : Group.values())
            {
                data.put(g.getName(), new String[] {"vc.legacy."+g.getName()});
            }

            FileWriter writer = new FileWriter(permsJson, false);
            writer.write(data.toJSONString());
            writer.flush();
            writer.close();
        }
    }
}
