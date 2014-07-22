package net.vaultcraft.vcessentials.file;

import net.vaultcraft.vcessentials.VCEssentials;
import net.vaultcraft.vcutils.protection.Area;
import net.vaultcraft.vcutils.protection.ProtectedArea;
import net.vaultcraft.vcutils.protection.ProtectionManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Connor on 7/21/14. Designed for the VCUtils project.
 */

public class ProtectionFile {

    private static ProtectionFile instance;

    public static ProtectionFile getInstance() {
        try {
            return (instance == null ? (instance = new ProtectionFile()) : instance);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static File file = new File(VCEssentials.getInstance().getDataFolder().getAbsolutePath(), "protection.json");

    public ProtectionFile() throws IOException, ParseException {
        if (file == null)
            file = new File(VCEssentials.getInstance().getDataFolder().getAbsolutePath(), "protection.json");
        if (!(VCEssentials.getInstance().getDataFolder().exists()))
            VCEssentials.getInstance().getDataFolder().mkdirs();
        if (!(file.exists()))
            VCEssentials.getInstance().saveResource("protection.json", false);

        JSONParser parser = new JSONParser();
        JSONObject data = (JSONObject) parser.parse(new FileReader(file));
        if (data.isEmpty())
            return;

        JSONArray all = (JSONArray) data.get("regions");

        Iterator regions = all.iterator();
        while (regions.hasNext()) {
            JSONObject obj = (JSONObject)regions.next();
            String name = obj.get("name").toString();

            World world = Bukkit.getWorld(obj.get("world").toString());
            int xMin = Integer.valueOf(obj.get("xMin").toString());
            int yMin = Integer.valueOf(obj.get("yMin").toString());
            int zMin = Integer.valueOf(obj.get("zMin").toString());

            int xMax = Integer.valueOf(obj.get("xMax").toString());
            int yMax = Integer.valueOf(obj.get("yMax").toString());
            int zMax = Integer.valueOf(obj.get("zMax").toString());

            Location min = new Location(world, xMin, yMin, zMin);
            Location max = new Location(world, xMax, yMax, zMax);
            ProtectionManager.getInstance().addToProtection(name, new ProtectedArea(new Area(min, max)));
        }
    }

    public File getFile() {
        return file;
    }

    public void saveAll() {
        try {
            new PrintWriter(file).close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JSONObject obj = new JSONObject();
        JSONArray regions = new JSONArray();

        HashMap<String, ProtectedArea> pr = ProtectionManager.getInstance().getRegions();
        for (String key : pr.keySet()) {
            ProtectedArea area = pr.get(key);

            JSONObject rObj = new JSONObject();
            rObj.put("name", key);
            rObj.put("world", area.getArea().getMax().getWorld().getName());

            Location min = area.getArea().getMin();
            Location max = area.getArea().getMax();

            int xMin = min.getBlockX();
            int yMin = min.getBlockY();
            int zMin = min.getBlockZ();

            int xMax = max.getBlockX();
            int yMax = max.getBlockY();
            int zMax = max.getBlockZ();

            rObj.put("xMin", xMin);
            rObj.put("yMin", yMin);
            rObj.put("zMin", zMin);

            rObj.put("xMax", xMax);
            rObj.put("yMax", yMax);
            rObj.put("zMax", zMax);

            regions.add(rObj);
        }

        FileWriter out = null;
        try {
            out = new FileWriter(file);
            out.write(obj.toJSONString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
