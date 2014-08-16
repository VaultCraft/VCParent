package net.vaultcraft.vcutils.item.menu;

import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by Connor on 8/15/14. Designed for the VCUtils project.
 */

public class MenuManager {

    private static HashMap<String, MenuModel> menus = new HashMap<>();
    private static HashMap<Player, ActiveMenu> active = new HashMap<>();

    public static void addMenuModel(String title, MenuModel menu) {
        menus.put(title, menu);
    }

    public static void addActiveMenu(Player player, ActiveMenu menu) {
        active.put(player, menu);
    }

    public static MenuModel fromTitle(String title) {
        return menus.get(title);
    }

    public static ActiveMenu fromPlayer(Player player) {
        return active.get(player);
    }

    public static HashMap<String, MenuModel> allModels() {
        return menus;
    }

    public static HashMap<Player, ActiveMenu> allActive() {
        return active;
    }
}
