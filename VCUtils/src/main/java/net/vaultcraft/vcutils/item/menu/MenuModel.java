package net.vaultcraft.vcutils.item.menu;

import net.vaultcraft.vcutils.item.menu.behavior.CloseBehavior;
import net.vaultcraft.vcutils.item.menu.behavior.OpenBehavior;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by Connor on 8/14/14. Designed for the VCUtils project.
 */

public class MenuModel {

    private int size;
    private String title;
    private MenuItem[] items = {};
    private OpenBehavior openBehavior;
    private CloseBehavior closeBehavior;

    public MenuModel(String title, int rows) {
        this.title = title;
        this.size = (rows*9 > 54 ? 54 : rows*9);
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public MenuItem[] items() {
        return items;
    }

    public MenuModel setOpenBehavior(OpenBehavior behavior) {
        this.openBehavior = behavior;
        return this;
    }

    public MenuModel setCloseBehavior(CloseBehavior behavior) {
        this.closeBehavior = behavior;
        return this;
    }

    public MenuModel addItem(MenuItem item) {
        MenuItem[] copy = Arrays.copyOf(items, items.length+1);
        copy[copy.length-1] = item;
        this.items = copy;
        return this;
    }

    public void onOpen(Player player) {
        this.openBehavior.onOpen(player);
    }

    public void onClose(Player player) {
        this.closeBehavior.onClose(player);
    }
}
