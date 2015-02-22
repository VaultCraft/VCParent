package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcessentials.VCEssentials;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Connor on 8/31/14. Designed for the VCUtils project.
 */

public class VCServer extends ICommand {

    private static ArrayList<VCServerConfigItem> server_map = new ArrayList<>();

    public VCServer(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
        ConfigurationSerialization.registerClass(VCServerConfigItem.class);
        YamlConfiguration serversConfig = YamlConfiguration.loadConfiguration(new File(VCEssentials.getInstance().getDataFolder(), "servers.yml"));

        if(!serversConfig.contains("servers"))
        {
            server_map.add(new VCServerConfigItem(Material.NOTE_BLOCK, "&5&lVault &d&lLobby", "hub", "&a➤ &a&oClick to connect!"));
            server_map.add(new VCServerConfigItem(Material.IRON_FENCE, "&5&lVault &f&lPrison", "prison", "&a➤ &a&oClick to connect!"));
            server_map.add(new VCServerConfigItem(Material.TNT, "&5&lVault &4&lGuilds", "guilds", "&c➤ &c&oComing soon."));
            server_map.add(new VCServerConfigItem(Material.GOLDEN_APPLE, "&5&lVault &6&lKit PvP", "kitpvp", "&c➤ &c&oComing soon."));
            server_map.add(new VCServerConfigItem(Material.COMMAND, "&5&lVault &e&lArcade", "arcade", "&c➤ &c&oComing soon."));

            serversConfig.set("servers", server_map);
            try {
                serversConfig.save(new File(VCEssentials.getInstance().getDataFolder(), "servers.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            server_map = (ArrayList<VCServerConfigItem>) serversConfig.get("servers");
        }

        Bukkit.getPluginManager().registerEvents(new VCServerListener(), VCEssentials.getInstance());
    }

    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            //open GUI etc...
            Inventory inv = Bukkit.createInventory(player, (server_map.size()+(9-(server_map.size()%9))), ChatColor.DARK_PURPLE+"Game Selector");
            for(VCServerConfigItem i : server_map)
            {
                inv.addItem(i.toItemStack());
            }
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
                VCEssentials.getInstance().sendPlayerToServer((Player)event.getWhoClicked(), server_map.get(event.getSlot()).server);
                event.setCancelled(true);
            }
        }
    }
}
