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

/**
 * Created by Connor on 8/31/14. Designed for the VCUtils project.
 */

public class VCServer extends ICommand {

    public VCServer(String name, Group permission, String... aliases) {
        super(name, permission, aliases);

        Bukkit.getPluginManager().registerEvents(new VCServerListener(), VCEssentials.getInstance());
    }

    private static HashMap<ItemStack, String> server_map = new HashMap<>();
    static {
        server_map.put(ItemUtils.build(Material.IRON_FENCE, "&5&lVault &7&lPrison", "&a➤ Click to connect!"), "prison");
        server_map.put(ItemUtils.build(Material.PORTAL, "&5&lVault &6&lLobby", "&a➤ Click to connect!"), "hub");
    }

    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            //open GUI etc...
            Inventory inv = Bukkit.createInventory(player, (server_map.size()+(9-(server_map.size()%9))), ChatColor.DARK_PURPLE+"Select a server");
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
            if (inv.getName().equals(ChatColor.DARK_PURPLE+"Select a server")) {
                VCEssentials.getInstance().sendPlayerToServer((Player)event.getWhoClicked(), server_map.get(event.getCurrentItem()));
                event.setCancelled(true);
            }
        }
    }
}
