package net.vaultcraft.vcessentials.blocks;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.item.ItemSerializer;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
        CHEST_SWITCHING,
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
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.ENDER_CHEST) {
            User networkUser = User.fromPlayer(e.getPlayer());
            if (!activeUsers.containsKey(networkUser)) {
                e.setCancelled(true);
                activeUsers.put(networkUser, EnderChestState.CHEST_MENU);
                e.getPlayer().openInventory(getEnderMenuForUser(networkUser));
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) { // This is called for every inventory, even custom ones.
        if(activeUsers.containsKey(User.fromPlayer((org.bukkit.entity.Player) e.getPlayer()))) {
            if(activeUsers.get(User.fromPlayer((org.bukkit.entity.Player) e.getPlayer())) == EnderChestState.CHEST_SWITCHING) {
                activeUsers.put(User.fromPlayer((org.bukkit.entity.Player) e.getPlayer()), EnderChestState.CHEST_INVENTORY);
                return;
            } else if(activeUsers.get(User.fromPlayer((org.bukkit.entity.Player) e.getPlayer())) == EnderChestState.CHEST_INVENTORY) {
                int invNum = Integer.parseInt(e.getInventory().getName().split("#")[1]); // This feels so hacky
                JSONArray myCoolArray = new JSONArray();
                for(ItemStack i : e.getInventory()) {
                    myCoolArray.add(ItemSerializer.fromStack(i));
                }
                User.fromPlayer((org.bukkit.entity.Player) e.getPlayer()).addUserdata("EChestInv"+invNum, myCoolArray.toJSONString());

            }
            activeUsers.remove(User.fromPlayer((org.bukkit.entity.Player) e.getPlayer()));

        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(activeUsers.containsKey(User.fromPlayer((org.bukkit.entity.Player) e.getWhoClicked()))
                && activeUsers.get(User.fromPlayer((org.bukkit.entity.Player) e.getWhoClicked())) == EnderChestState.CHEST_MENU) {
            User clickingUser = User.fromPlayer((org.bukkit.entity.Player) e.getWhoClicked());
            EnderChestInventory clickedInv = EnderChestInventory.getForUser(e.getRawSlot(), clickingUser);
            switch (clickedInv.getCurrState()) {
                default:
                    activeUsers.put(clickingUser, EnderChestState.CHEST_SWITCHING);
                    e.getWhoClicked().openInventory(clickedInv.getInventory());
                    break;
                case INV_NORANK:
                    Form.at((org.bukkit.entity.Player) e.getWhoClicked(), Prefix.ERROR, "You do not have permission to access that inventory.");
                    e.getWhoClicked().closeInventory();
                    break;
            }
            e.setCancelled(true);
        }
    }

    private Inventory getEnderMenuForUser(User user) {
        Inventory base = Bukkit.createInventory(null, 54, "The Ender Storage Realm.");
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