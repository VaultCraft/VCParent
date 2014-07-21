package net.vaultcraft.vcutils.string;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;

import java.util.List;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class StringUtils {

    public static String buildFromArray(String[] array) {
        return buildFromArray(array, 0);
    }

    public static String buildFromArray(String[] array, int start) {
        String compose = "";
        for (int x = start; x < array.length; x++) {
            compose+=array[x]+" ";
        }
        return compose;
    }

    public static Object type(String input) {
        input = input;
        if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false"))
            return Boolean.valueOf(input);

        if (input.contains("."))
            try { return Double.parseDouble(input); } catch (Exception ex) {}
        try { return Integer.parseInt(input); } catch (Exception ex) {}
        if (input.contains(",")) {
            List<String> create = Lists.newArrayList();
            for (String up : input.split(","))
                create.add(ChatColor.translateAlternateColorCodes('&', up.replace("_", " ")));
            return create;
        }

        return input.replace("_", " ");
    }
}
