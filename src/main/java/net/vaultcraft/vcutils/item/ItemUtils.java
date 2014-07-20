package net.vaultcraft.vcutils.item;

import net.vaultcraft.vcutils.VCUtils;
import org.arkhamnetwork.survivalgames.SurvivalGamesPlugin;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * Created by tacticalsk8er on 4/12/14.
 */
public class ItemUtils {

    static Plugin plugin = VCUtils.

    public static ItemStack setLore(ItemStack itemStack, List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack setName(ItemStack itemStack, String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack setLeatherArmorColor(ItemStack itemStack, Color color) {
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
        leatherArmorMeta.setColor(color);
        itemStack.setItemMeta(leatherArmorMeta);
        return itemStack;
    }

    public static ItemStack setLeatherArmorColor(ItemStack itemStack, int r, int g, int b) {
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
        leatherArmorMeta.setColor(Color.fromRGB(r, g, b));
        itemStack.setItemMeta(leatherArmorMeta);
        return itemStack;
    }
}
