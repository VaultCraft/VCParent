package net.vaultcraft.vcutils.item.inventory;

import org.bukkit.inventory.Inventory;

import java.util.HashMap;

/**
 * Created by Connor on 7/23/14. Designed for the VCUtils project.
 */

public class Menu {

    private Menu parent;
    private HashMap<String, Menu> children = new HashMap<>();
    private Inventory actual;

    public Menu(Inventory actual) {
        this.actual = actual;
    }

    public Menu openChild(String node) {
        if (children.containsKey(node))
            return children.get(node);

        return null;
    }

    public Menu openParent() {
        return parent;
    }

    public Inventory getActual() {
        return actual;
    }
}
