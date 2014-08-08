package net.vaultcraft.vcutils.sign;

import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.GsonBuilder;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.file.FileController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Exception;import java.lang.Integer;import java.lang.Override;import java.lang.String;import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Connor on 8/8/14. Designed for the VCUtils project.
 */

public class SignLoader implements FileController {

    private static SignLoader instance;
    private static File file = new File(VCUtils.getInstance().getDataFolder().getAbsolutePath(), "signs.json");

    public SignLoader() {
        instance = this;
    }

    public static FileController getInstance() {
        return instance;
    }

    public File getFile() { return file; }

    @Override
    public void load() {
        if (file == null)
            file = new File(VCUtils.getInstance().getDataFolder().getAbsolutePath(), "signs.json");
        if (!(VCUtils.getInstance().getDataFolder().exists()))
            VCUtils.getInstance().getDataFolder().mkdirs();
        if (!(file.exists()))
            VCUtils.getInstance().saveResource("signs.json", false);

        JSONParser parser = new JSONParser();
        JSONObject data = null;
        try {
            data = (JSONObject) parser.parse(new FileReader(file));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        if (data.isEmpty())
            return;

        JSONArray all = (JSONArray)data.get("signs");

        Iterator it = all.iterator();
        while (it.hasNext()) {
            JSONObject obj = (JSONObject)it.next();
            String[] text = (obj.get("text").toString().split("%newline%"));

            World world = Bukkit.getWorld(obj.get("world").toString());
            int x = Integer.valueOf(obj.get("x").toString());
            int y = Integer.valueOf(obj.get("y").toString());
            int z = Integer.valueOf(obj.get("z").toString());

            Location location = new Location(world, x, y, z);
            if (location.getBlock().getState() instanceof Sign) {
                Sign sign = (Sign)location.getBlock().getState();
                int $ = 0;
                for (String line : text) {
                    if ($ >= 4)
                        continue;

                    sign.setLine($, ChatColor.translateAlternateColorCodes('&', line));
                    $++;
                }
                sign.update();

                SignManager.addSign(sign, obj.get("meta").toString());
            }
        }
    }

    @Override
    public void save() {
        try {
            JSONObject obj = new JSONObject();
            JSONArray signs = new JSONArray();

            HashMap<String, List<Sign>> all = SignManager.all();
            for (String key : all.keySet()) {
                List<Sign> values = all.get(key);
                for (Sign value : values) {
                    JSONObject sObj = new JSONObject();
                    sObj.put("meta", key);
                    String text = "";
                    int pos = 0;
                    for (String line : value.getLines()) {
                        if (pos++ >= 4)
                            text+=line;
                        else
                            text+=(line+"%newline%");
                    }
                    sObj.put("text", text);

                    Location loc = value.getLocation();
                    sObj.put("world", loc.getWorld().getName());
                    sObj.put("x", loc.getBlockX()+"");
                    sObj.put("y", loc.getBlockY()+"");
                    sObj.put("z", loc.getBlockZ()+"");

                    signs.add(sObj);
                }
            }
            obj.put("signs", signs);
            JSONParser parser = new JSONParser();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            String jsonString = gson.toJson(parser.parse(obj.toJSONString()));

            FileWriter out = null;
            out = new FileWriter(file);
            out.write(jsonString);
            out.flush();
            out.close();

        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
