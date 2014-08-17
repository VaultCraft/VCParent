package net.vaultcraft.vcessentials.blocks;

import net.vaultcraft.vcessentials.VCEssentials;
import net.vaultcraft.vcutils.database.sql.Statements;
import net.vaultcraft.vcutils.item.ItemSerializer;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.EnderChest;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Enderchest thing. Super untested.
 */
public class BEnderChest implements Listener {
    private enum EnderChestState {
        CHEST_MENU,
        CHEST_INVENTORY,
        INV_NORANK(15),
        INV_EMPTY(0),
        INV_ONETHIRD(5),
        INV_TWOTHIRD(4),
        INV_THREETHIRD(1),
        INV_FULL(14);

        private short glassColor = -1;

        private EnderChestState() {

        }

        private EnderChestState(int glassColor) {
            this.glassColor = (short) glassColor;
        }

        public short getGlassColor() {
            return glassColor;
        }
    }

    private static class EnderChestInventory {

        public final int INV_SIZE = 27;

        private int slot = 0;
        private List<ItemStack> contents;
        private User user;
        private EnderChestInventory(int slot, List<ItemStack> contents, User user) {
            this.slot = slot;
            this.contents = contents;
            this.user = user;
        }

        public int getItemCount() {
            return contents.size();
        }

        public EnderChestState getCurrState() {
            if (user.getGroup().getEnderChestSlots() < slot) {
                return EnderChestState.INV_NORANK;
            }
            int howFull = getItemCount() / INV_SIZE;
            if (howFull == 0) {
                return EnderChestState.INV_EMPTY;
            } else if(howFull <= 0.33) {
                return EnderChestState.INV_ONETHIRD;
            } else if(howFull <= 0.66) {
                return EnderChestState.INV_TWOTHIRD;
            } else if(howFull <= 0.99) {
                return EnderChestState.INV_THREETHIRD;
            } else {
                return EnderChestState.INV_FULL;
            }

        }

        public Inventory getInventory() {
            Inventory inv = Bukkit.createInventory(null, INV_SIZE, "Ender Chest #"+slot); // TODO actually pass owner in
            for(ItemStack i : contents) {
                inv.addItem(i);
            }
            return inv;
        }

        public static EnderChestInventory getForUser(int slot, User user) {
            JSONParser userDataParser = new JSONParser();
            String jsonString = user.getUserdata("EChestInv"+slot);
            if (jsonString == null) {
                return new EnderChestInventory(slot, new ArrayList<ItemStack>(), user);
            }
            try {
                Object obj = userDataParser.parse(jsonString);
                JSONArray itemStrings = (JSONArray) obj;
                ArrayList<ItemStack> contents = new ArrayList<>();
                for (Object itemString : itemStrings) {
                    String itemStr = (String) itemString;
                    contents.add(ItemSerializer.fromString(itemStr));
                }
                return new EnderChestInventory(slot, contents, user);
            } catch (ParseException e) {
                return new EnderChestInventory(slot, new ArrayList<ItemStack>(), user);
            }
        }

    }

    private HashMap<User, EnderChestState> activeUsers = new HashMap<>();

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (!(e.getInventory().getHolder() instanceof EnderChest)) {
            return;
        }
        User networkUser = User.fromPlayer((org.bukkit.entity.Player) e.getPlayer());
        if (!activeUsers.containsKey(networkUser)) {
            e.setCancelled(true);
            activeUsers.put(networkUser, EnderChestState.CHEST_MENU);
            e.getPlayer().openInventory(getEnderMenuForUser(networkUser, e.getInventory().getHolder()));
        }
    }

    private Inventory getEnderMenuForUser(User user, InventoryHolder parent) {
        Inventory base = Bukkit.createInventory(parent, 54, "The Ender Storage Realm.");
        for (int i = 0; i < 54; i++) {
            final EnderChestInventory thisInv = EnderChestInventory.getForUser(i, user);
            short statusColor = thisInv.getCurrState().getGlassColor();
            ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, statusColor);
            ItemMeta glassMeta = glass.getItemMeta();
            glassMeta.setDisplayName("Ender Inventory #"+i);
            glassMeta.setLore(new ArrayList<String>() {{
            add(thisInv.getItemCount() + " / " + thisInv.INV_SIZE + "Slots filled.");
            }});
            glass.setItemMeta(glassMeta);
            base.addItem(glass);
        }

        return base;
    }

}