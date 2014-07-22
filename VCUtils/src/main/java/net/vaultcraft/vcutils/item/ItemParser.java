package net.vaultcraft.vcutils.item;

import net.vaultcraft.vcutils.string.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class ItemParser {

    public static ItemStack fromString(String input) {
        Map<String, Object> kv = new HashMap<>();
        String[] split = input.split(" ");
        for (String breakUp : split) {
            String key = breakUp.split(":")[0];
            String value = breakUp.split(":")[1];

            if (value.contains("@")) {
                //is enchantment hash
                HashMap<Enchantment, Integer> enchMap = new HashMap<>();
                for (String ench : value.split(",")) {
                    Enchantment id = Enchantment.getById(Integer.parseInt(ench.split("@")[0]));
                    int amount = Integer.parseInt(ench.split("@")[1]);
                    enchMap.put(id, amount);
                }
                kv.put(key, enchMap);
                continue;
            }

            kv.put(key, StringUtils.type(value));
        }

        int id = (Integer)kv.get("id");
        int amount = (kv.containsKey("amount") ? (Integer)kv.get("amount") : 1);
        byte data = (kv.containsKey("data") ? ((Integer)kv.get("data")).byteValue() : (byte)0);

        ItemStack stack = new ItemStack(Material.getMaterial(id), amount, (short)0, data);
        ItemMeta meta = stack.getItemMeta();

        if (kv.containsKey("name"))
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', (String)kv.get("name")));
        if (kv.containsKey("lore"))
            meta.setLore((List<String>)kv.get("lore"));

        stack.setItemMeta(meta);
        if (kv.containsKey("enchantments"))
            stack.addUnsafeEnchantments((HashMap<Enchantment, Integer>)kv.get("enchantments"));

        return stack;
    }
}
