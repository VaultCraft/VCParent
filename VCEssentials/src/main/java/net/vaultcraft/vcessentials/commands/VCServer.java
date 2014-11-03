package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcessentials.VCEssentials;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.item.ItemUtils;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Connor on 8/31/14. Designed for the VCUtils project.
 */

public class VCServer extends ICommand {

    public VCServer(String name, Group permission, String... aliases) {
        super(name, permission, aliases);

        Bukkit.getPluginManager().registerEvents(new VCServerListener(), VCEssentials.getInstance());
    }

    private static LinkedHashMap<ItemStack, String> server_map = new LinkedHashMap<>();
    static {
        server_map.put(ItemUtils.build(Material.NOTE_BLOCK, "&5&lVault &d&lLobby", "&a➤ &a&oClick to connect!"), "hub");
        server_map.put(ItemUtils.build(Material.IRON_FENCE, "&5&lVault &f&lPrison", "&a➤ &a&oClick to connect!"), "prison");
        server_map.put(ItemUtils.build(Material.GOLDEN_APPLE, "&5&lVault &6&lKit PvP", "&c➤ &c&oComing soon."), "kitpvp");
        server_map.put(ItemUtils.build(Material.TNT, "&5&lVault &4&lFactions", "&c➤ &c&oComing soon."), "factions");
        server_map.put(ItemUtils.build(Material.COMMAND, "&5&lVault &c&lArcade", "&c➤ &c&oComing soon."), "arcade");
    }

    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            //open GUI etc...
            Inventory inv = Bukkit.createInventory(player, (server_map.size()+(9-(server_map.size()%9))), ChatColor.DARK_PURPLE+"Game Selector");
            inv.addItem(server_map.keySet().toArray(new ItemStack[server_map.size()]));
            player.openInventory(inv);
        } else {
            VCEssentials.getInstance().sendPlayerToServer(player, args[0].toLowerCase());
        }
    }

    private static class VCServerListener implements Listener {
        @EventHandler
        public void onGUIClick(InventoryClickEvent event) {
            Inventory inv = event.getInventory();
            if (inv.getName().equals(ChatColor.DARK_PURPLE+"Game Selector")) {
                VCEssentials.getInstance().sendPlayerToServer((Player)event.getWhoClicked(), server_map.get(event.getCurrentItem()));
                event.setCancelled(true);
            }
        }
    }
}
