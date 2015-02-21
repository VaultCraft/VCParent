package net.vaultcraft.vcutils.user.permission;

import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BukkitPermissionsFile {

    public HashMap<String, List<String>> permissions;
    private YamlConfiguration configFile;
    private File configYml;

    public BukkitPermissionsFile() throws Exception {
        this(new File(VCUtils.getInstance().getDataFolder().getAbsolutePath(), "permissions.yml"));
    }

    public BukkitPermissionsFile(File file) throws Exception {
        permissions = new HashMap<>();

        configYml = file;
        if(!configYml.exists())
        {
            try {
                //noinspection ResultOfMethodCallIgnored
                configYml.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        configFile = YamlConfiguration.loadConfiguration(configYml);

        for(Group g : Group.values()) {
            if(!configFile.contains("group."+g.getName()))
            {
                configFile.set("group."+g.getName(), Arrays.asList("vc.legacy."+g.getName()));
            }

            permissions.put(g.getName(), (List<String>) configFile.get("group."+g.getName()));
        }

        configFile.save(configYml);

    }


}
