package net.vaultcraft.vcutils.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * @author Connor Hollasch
 * @since 12/29/2014
 */
public class ConfigLocation {

    private double x;
    private double y;
    private double z;

    private float pitch;
    private float yaw;

    private World world;

    private File file;
    private YamlConfiguration conf;

    private String section;

    private Location location;

    public ConfigLocation(Plugin plugin, String config, String section) {
        this.section = section;

        file = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + config);
        if (!file.exists())
            return;

        conf = YamlConfiguration.loadConfiguration(file);
        if (conf == null)
            return;

        load(conf, section);
    }

    private void load(FileConfiguration conf, String section) {
        if (!conf.contains(section))
            return;

        String parse = conf.get(section).toString();
        if (!parse.contains(","))
            return;

        String[] parts = parse.split(",");
        x = Double.parseDouble(parts[1]);
        y = Double.parseDouble(parts[2]);
        z = Double.parseDouble(parts[3]);

        yaw = Float.parseFloat(parts[4]);
        pitch = Float.parseFloat(parts[5]);

        world = Bukkit.getWorld(parts[0]);

        location = new Location(world, x, y, z, yaw, pitch);
    }

    public void save() {
        conf.set(section, world.getName()+","+x+","+y+","+z+","+yaw+","+pitch);

        try {
            conf.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getLocation() {
        return location;
    }
}
