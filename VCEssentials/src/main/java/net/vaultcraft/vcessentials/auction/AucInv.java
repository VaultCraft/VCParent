package net.vaultcraft.vcessentials.auction;

import com.google.common.collect.Lists;
import net.vaultcraft.vcessentials.VCEssentials;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.item.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author Connor Hollasch
 * @since 11/2/2014
 */
public class AucInv implements Listener {

    private static HashMap<Integer, Inventory> pages = new HashMap<>();
    private static HashMap<Player, Integer> opened = new HashMap<>();

    private static final ItemStack PREV_PAGE = ItemUtils.build(Material.ARROW, "&f&m&l<-&r&a&l Previous Page");
    private static final ItemStack NEXT_PAGE = ItemUtils.build(Material.ARROW, "&a&lNext Page&r &f&m&l-&r&f&l>");

    public AucInv() {
        Bukkit.getPluginManager().registerEvents(this, VCEssentials.getInstance());
        update();
    }

    public static void update() {
        pages.clear();

        List<Auction> auctions = fromHashSet(AucManager.getAuctions());
        int totalAuctions = auctions.size();
        int totalPages = (int)Math.ceil(totalAuctions/45);
        for (int i = 0; i < totalPages; ++i) {
            pages.put(i, Bukkit.createInventory(null, 54, ChatColor.GOLD+"Auctions"));
        }

        if (!(pages.containsKey(1)))
            pages.put(1, Bukkit.createInventory(null, 54, ChatColor.GOLD+"Auctions"));

        int pos = 0;
        for (int page : pages.keySet()) {
            Inventory value = pages.get(page);

            if (page == 1) {
                if (pages.size() > 1)
                    value.setItem(8, NEXT_PAGE);

            } else if (page == pages.size()) {
                value.setItem(0, PREV_PAGE);
            } else {
                value.setItem(0, PREV_PAGE);
                value.setItem(8, NEXT_PAGE);
            }

            for (int i = 0; i < 45; i++) {
                if (auctions.size() <= pos)
                    return;

                Auction get = auctions.get(pos);

                ItemStack selling = get.getSellingStack().clone();

                ItemMeta meta = selling.getItemMeta();
                if (meta.getLore() == null)
                    meta.setLore(Lists.newArrayList());

                List<String> lore = meta.getLore();

                lore.add(ChatColor.translateAlternateColorCodes('&', "&c&m---------------------------"));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&9&lSOLD BY&f: &7" + get.getCreator().getName()));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&9&lCURRENT BID&f: &7" + Form.at(get.getCurrentBid(), true)));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&9&lCURRENT BIDDER&f: &7" + (get.getCurrentBidder() == null ? "No one" : get.getCurrentBidder().getName())));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&9&lTIME LEFT&f: &7" + (get.getEndingTime() - System.currentTimeMillis())/60000 + " minutes"));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&c&m---------------------------"));

                meta.setLore(lore);
                selling.setItemMeta(meta);

                value.setItem(9+i, selling);
                ++pos;
            }
        }
    }

    public static void open(Player player) {
        update();
        opened.put(player, 1);
        player.openInventory(pages.get(1));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!opened.containsKey(event.getWhoClicked()))
            return;

        event.setCancelled(true);

        if (event.getCurrentItem() == null)
            return;

        if (event.getCurrentItem().equals(NEXT_PAGE)) {
            //next page
            int at = opened.get(event.getWhoClicked())+1;
            event.getWhoClicked().closeInventory();
            event.getWhoClicked().openInventory(pages.get(at));
            opened.put((Player)event.getWhoClicked(), at);
        } else if (event.getCurrentItem().equals(PREV_PAGE)) {
            //prev page
            event.getWhoClicked().closeInventory();
            int at = opened.remove(event.getWhoClicked())-1;
            event.getWhoClicked().openInventory(pages.get(at));
            opened.put((Player)event.getWhoClicked(), at);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (opened.containsKey(event.getPlayer()))
            opened.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        opened.remove(event.getPlayer());
    }

    private static List<Auction> fromHashSet(HashSet<Auction> aucs) {
        List<Auction> l = Lists.newArrayList();
        for (Auction auc : aucs) {
            l.add(auc);
        }
        return l;
    }
}
