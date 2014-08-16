package net.vaultcraft.vcutils.item.menu;

import net.vaultcraft.vcutils.item.menu.behavior.ClickBehavior;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Connor on 8/15/14. Designed for the VCUtils project.
 */

public class MenuItem {

    private ClickBehavior clickBehavior;

    private int activeState = 0;
    private Map<Integer, ItemStack> itemStates = new HashMap<>();
    private int slot;

    public MenuItem(ItemStack... stacks) {
        int x = 0;
        for (ItemStack stack : stacks) {
            itemStates.put(x, stack);
            x++;
        }
    }

    public MenuItem switchToState(int state) {
        this.activeState = state;
        return this;
    }

    public ItemStack getActiveStack() {
        return itemStates.get(activeState);
    }

    public MenuItem setClickBehavior(ClickBehavior behavior) {
        this.clickBehavior = behavior;
        return this;
    }

    public boolean onClick(Player player) {
        if (clickBehavior == null)
            return true;

        return this.clickBehavior.onClick(player);
    }

    public void setItemState(int slot, ItemStack newStack) {
        itemStates.remove(slot);
        itemStates.put(slot, newStack);
    }

    public int getSlot() {
        return slot;
    }
}
