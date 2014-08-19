package net.vaultcraft.vcutils.item.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;


/**
 * Created by Connor on 8/15/14. Designed for the VCUtils project.
 */

public class ActiveMenu {

    private Player holder;
    private MenuModel model;

    private MenuItem[] items;
    private Inventory open;

    public ActiveMenu(Player holder, MenuModel model) {
        this.holder = holder;
        this.model = model;

        this.items = Arrays.copyOf(model.items(), model.items().length);
    }

    public ActiveMenu openInventory() {
        Inventory inv = Bukkit.createInventory(holder, model.getSize(), ChatColor.translateAlternateColorCodes('&', model.getTitle()));

        for (MenuItem item : items) {
            if (item == null)
                continue;

            inv.setItem(item.getSlot(), item.getActiveStack());
        }

        this.open = inv;
        return this;
    }

    public void update() {
        for (MenuItem item : items) {
            open.setItem(item.getSlot(), item.getActiveStack());
        }
    }

    public void closeInventory() {
        holder.closeInventory();
        this.open = null;
    }

    public void setState(MenuItem item) {
        int pos = Arrays.binarySearch(items, item);
        items[pos] = item;
    }

    public MenuItem fromSlot(int slot) {
        for (MenuItem item : items) {
            if (item.getSlot() == slot)
                return item;
        }

        return null;
    }

    public void addMenuItem(MenuItem item) {
        MenuItem[] clone = Arrays.copyOf(items, items.length+1);
        clone[clone.length-1] = item;
        this.items = clone;
    }

    public void removeMenuItem(MenuItem item) {
        int pos = Arrays.binarySearch(items, item);
        if (pos < 0)
            return;

        items[pos] = null;
    }

    public void removeMenuItem(int slot) {
        removeMenuItem(fromSlot(slot));
    }

    public MenuModel getModel() {
        return model;
    }
}
