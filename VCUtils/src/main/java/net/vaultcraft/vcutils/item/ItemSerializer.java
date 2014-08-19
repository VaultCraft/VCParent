package net.vaultcraft.vcutils.item;

import com.google.common.collect.Lists;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Created by Connor on 8/16/14. Designed for the VCUtils project.
 */

public class ItemSerializer {

    public static String fromStack(ItemStack stack) {
        int id = stack.getTypeId();
        int data = (int)(stack.getData().getData());
        int amount = stack.getAmount();
        String display = (stack.getItemMeta() == null ? null : (stack.getItemMeta().getDisplayName() == null ? null : stack.getItemMeta().getDisplayName()));
        List<String> lore = (stack.getItemMeta() == null ? null : (stack.getItemMeta().getLore() == null ? null : stack.getItemMeta().getLore()));
        Map<Enchantment, Integer> enchantments = stack.getEnchantments();

        String build = "@t"+id+"@d"+data+"@a"+amount+(display == null ? "" : "@n"+display)+(lore == null ? "" : "@l"+fromList(lore))+(enchantments.size() == 0 ? "" : "@e"+fromMap(enchantments));
        return build;
    }

    private static String fromList(List<String> list) {
        String build = "";
        for (String s : list) {
            build+=(s+",");
        }

        return (build.charAt(build.length()-1) == ',' ? build.substring(0, build.length()-1) : build);
    }

    private static String fromMap(Map<Enchantment, Integer> map) {
        String build = "";
        for (Enchantment key : map.keySet()) {
            int value = map.get(key);
            build+=(key.getId()+"%"+value+"_");
        }

        return (build.charAt(build.length()-1) == '_' ? build.substring(0, build.length()-1) : build);
    }

    public static ItemStack fromString(String input) {
        int id = Integer.parseInt(within(input, "t"));
        byte data = Byte.valueOf(within(input, "d"));
        int amount = Integer.parseInt(within(input, "a"));
        String display = (input.contains("@n") ? within(input, "n") : null);
        List<String> lore = (input.contains("@l") ? Arrays.asList(within(input, "l").split(",")) : new ArrayList<String>());
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        if (input.contains("@e")) {
            String left = input.substring(input.indexOf("@e")+2);
            String[] map = left.split("_");
            for (String s : map) {
                enchantments.put(Enchantment.getById(Integer.parseInt(s.split("[\\%]")[0])), Integer.parseInt(s.split("[\\%]")[1]));
            }
        }

        ItemStack build = new ItemStack(id, amount, (short)0, data);
        ItemMeta meta = build.getItemMeta();
        if (display != null) meta.setDisplayName(display);
        if (lore.size() > 0) meta.setLore(lore);
        build.setItemMeta(meta);
        build.addUnsafeEnchantments(enchantments);

        return build;
    }

    private static String within(String check, String key) {

        int indexOf = check.indexOf("@"+key);
        int end = check.indexOf("@", indexOf+2);
        end = (end == -1 ? check.length() : end);

        return check.substring(indexOf+2, end);
    }
}
