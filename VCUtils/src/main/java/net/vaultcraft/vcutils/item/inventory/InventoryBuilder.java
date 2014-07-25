package net.vaultcraft.vcutils.item.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Connor on 4/21/14. Designed for the SurvivalGames project.
 */

public class InventoryBuilder {

    private Map<Character, ItemStack> shapes = new HashMap<>();
    private Map<Slot, Character> chars = new HashMap<>();

    public InventoryBuilder shape(String[]... shape) {
        for (int y = 0; y < shape.length; y++) {
            String[] row = shape[y];
            for (int x = 0; x < row.length; x++) {
                Slot loc = new Slot();
                loc.x = x;
                loc.y = y;

                char key = row[x].length() == 1 ? row[x].charAt(0) : ' ';
                chars.put(loc, key);
            }
        }
        return this;
    }

    public InventoryBuilder setChar(char key, ItemStack value) {
        if (shapes.containsKey(key))
            shapes.remove(key);

        shapes.put(key, value);
        return this;
    }

    public ItemStack getCharacter(char key) {
        return shapes.get(key);
    }

    public static InventoryBuilder builder() {
        return new InventoryBuilder();
    }

    public Inventory build(String name) {
        Inventory inv = null;
        if (name == null)
            inv = Bukkit.createInventory(null, chars.size());
        else
            inv = Bukkit.createInventory(null, chars.size(), ChatColor.translateAlternateColorCodes('&', name));

        for (Slot key : chars.keySet()) {
            char value = chars.get(key);
            ItemStack set = shapes.get(value);
            inv.setItem(key.getInvSlot(), set);
        }
        return inv;
    }

    private static class Slot {
        private int x;
        private int y;

        public int getInvSlot() {
            int ret = x;
            for (int q = 0; q < y; q++)
                ret += 9;
            return ret;
        }
    }
}

/**
 *
 * shape({"x","x","x","x","x","x","x","x","x"},
 *       {"x"," "," ","z","y","z"," "," ","x"},
 *       {"x","x","x","x","x","x","x","x","x"});
 *
 * setChar('x', new ItemStack(Material.IRON_INGOT));
 * setChar('z', new ItemStack(Material.ARROW));
 * setChar('y', new ItemStack(Materail.BOW));
 *
 * http://gyazo.com/2de33c4a986497e588ac8c4790273a63
 *
 */