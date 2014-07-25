package net.vaultcraft.vcutils.user.localdata;

import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.GsonBuilder;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by Connor on 7/24/14. Designed for the VCUtils project.
 *
 * Public methods should be called asynchronously
 */

public class DataManager {

    //ASYNC
    public static void populateUserdata(User user) {
        try {
            Player wrapped = user.getPlayer();

            File folder = new File(VCUtils.getInstance().getDataFolder().getAbsolutePath()+File.separator+"userdata");
            if (!(folder.exists()))
                folder.mkdirs();

            File load = new File(VCUtils.getInstance().getDataFolder().getAbsolutePath()+File.separator+"userdata", wrapped.getUniqueId().toString()+".json");
            if (!(load.exists())) {
                load.createNewFile();
                writeDefaultData(user, load);
            }

            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject)parser.parse(new FileReader(load));

            if (data.isEmpty())
                return;

            JSONArray all = (JSONArray) data.get("data");

            Iterator di = all.iterator();
            while (di.hasNext()) {
                String entry = (String)di.next();

                user.addUserdata(entry.split(":")[0], entry.split(":")[1]);
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private static void writeDefaultData(User user, File load) throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("data", new JSONArray());

        JSONParser parser = new JSONParser();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String jsonString = gson.toJson(parser.parse(obj.toJSONString()));

        FileWriter out = null;
        out = new FileWriter(load);
        out.write(jsonString);
        out.flush();
        out.close();
    }

    //ASYNC (pass in userdata manually for async method call on quit)
    public static void saveUserdata(HashMap<String, String> userdata, UUID playerUUID) {
        try {
            JSONObject obj = new JSONObject();
            JSONArray arr = new JSONArray();

            for (String key : userdata.keySet()) {
                String value = userdata.get(key);

                arr.add(key+":"+value);
            }

            obj.put("data", arr);

            JSONParser parser = new JSONParser();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            String jsonString = gson.toJson(parser.parse(obj.toJSONString()));

            FileWriter out = null;
            File folder = new File(VCUtils.getInstance().getDataFolder().getAbsolutePath()+File.separator+"userdata");
            if (!(folder.exists()))
                folder.mkdirs();

            out = new FileWriter(new File(VCUtils.getInstance().getDataFolder().getAbsolutePath()+File.separator+"userdata", playerUUID.toString()+".json"));
            out.write(jsonString);
            out.flush();
            out.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
