package net.vaultcraft.vcessentials.auction;

import com.google.common.collect.Lists;
import net.vaultcraft.vcessentials.VCEssentials;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.item.ItemUtils;
import net.vaultcraft.vcutils.user.OfflineUser;
import net.vaultcraft.vcutils.user.User;
import net.vaultcraft.vcutils.util.SignGUI;
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

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * @author Connor Hollasch
 * @since 11/2/2014
 */
public class AucInv implements Listener {

    private static HashMap<Integer, Inventory> pages = new HashMap<>();
    private static HashMap<Player, Integer> opened = new HashMap<>();

    private static HashMap<Player, Auction> viewing = new HashMap<>();

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

                value.setItem(9+i, addInfoLore(get));
                ++pos;
            }
        }
    }

    private static ItemStack addInfoLore(Auction get) {
        ItemStack selling = get.getSellingStack().clone();

        ItemMeta meta = selling.getItemMeta();
        if (meta.getLore() == null)
            meta.setLore(Lists.newArrayList());

        List<String> lore = meta.getLore();

        lore.add(ChatColor.translateAlternateColorCodes('&', "&c&m---------------------------"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&9&lSOLD BY&f: &7" + get.getCreator().getName()));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&9&lCURRENT BID&f: &7" + Form.at(get.getCurrentBid(), true)));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&9&lCURRENT BIDDER&f: &7" + (get.getCurrentBidder() == null ? "No one" : get.getCurrentBidder().getName())));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&9&lTIME LEFT&f: &7" + formatTime(get.getEndingTime() - System.currentTimeMillis())));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&0" + (get.getAuctionId().toString())));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&c&m---------------------------"));

        meta.setLore(lore);
        selling.setItemMeta(meta);

        return selling;
    }

    public static void open(Player player, OfflinePlayer other) {
        if (other != null) {
            HashSet<Auction> filtered = AucManager.filter((auc) -> auc.getCreator().equals(other));
            int slots = (int)((Math.ceil(filtered.size()/9))*9);
            if (slots < 9)
                slots = 9;

            Inventory build = Bukkit.createInventory(player, slots, ChatColor.GOLD+"Auctions");
            int pos = 0;
            for (Auction auc : filtered) {
                build.setItem(pos, addInfoLore(auc));
                ++pos;
            }

            player.openInventory(build);
            opened.put(player, -1);
            return;
        }

        update();
        opened.put(player, 1);
        player.openInventory(pages.get(1));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if (!(opened.containsKey(player) || viewing.containsKey(player)))
            return;

        event.setCancelled(true);

        if (event.getCurrentItem() == null)
            return;

        if (viewing.containsKey(player)) {
            Auction value = viewing.get(player);
            if (event.getCurrentItem().getType().equals(Material.GOLD_INGOT)) {
                if (value.getCurrentBid() > User.fromPlayer(player).getMoney()) {
                    Form.at(player, Prefix.ERROR, "You do not have enough money to bid on this!");
                    return;
                }

                if (value.getCreator().getPlayer().equals(player)) {
                    Form.at(player, Prefix.ERROR, "You cannot bid on your own auction!");
                    return;
                }

                viewing.remove(player);
                player.closeInventory();

                SignGUI.SignGUIListener bid = (player1, lines) -> {
                    if (lines[0] == null || lines[0].equals("")) {
                        Form.at(player1, Prefix.ERROR, "You didn't enter a valid price to bid!");
                        return;
                    }

                    double price = 0.0;
                    try {
                        price = Double.parseDouble(lines[0].replace("$", ""));
                    } catch (NumberFormatException ex) {
                        Form.at(player, Prefix.ERROR, "Could not get the number you bid!");
                        return;
                    }

                    if (value.getCurrentBid()+price > User.fromPlayer(player).getMoney()) {
                        Form.at(player, Prefix.ERROR, "You do not have enough money to bid on this!");
                        return;
                    }

                    if (price < value.getMinInterval()) {
                        Form.at(player, Prefix.ERROR, "You cannot bid less than $" + value.getMinInterval() + "!");
                        return;
                    }

                    OfflinePlayer oldBidder = value.getCurrentBidder();
                    if (oldBidder != null) {
                        if (oldBidder.isOnline()) {
                            Form.at(oldBidder.getPlayer(), Prefix.AUCTION, "You were outbid on one of your auctions! You received &e$" + value.getCurrentBid() + Prefix.AUCTION.getChatColor() + " back!");
                            User.fromPlayer(oldBidder.getPlayer()).addMoney(value.getCurrentBid());
                        } else
                            OfflineUser.getOfflineUser(oldBidder).addMoney(value.getCurrentBid());
                    }

                    value.addToCurrent(price, player);
                    player.closeInventory();
                    viewing.remove(player);
                    open(player, null);

                    User.fromPlayer(player).addMoney(-(value.getCurrentBid()));
                    Form.at(player, Prefix.WARNING, "A safety deposit of " + (value.getCurrentBid()) + " was taken from your balance!");
                    if (value.getCreator().isOnline()) {
                        Form.at(value.getCreator().getPlayer(), Prefix.AUCTION, "&e" + player.getName() + Prefix.AUCTION.getChatColor() + " has bid on your auction! Currently: &e$" + value.getCurrentBid());
                    }
                };
                VCUtils.getSignGUI().open(player, new String[]{"$", "Enter bid", "amount", ""}, bid);
                return;
            } else if (event.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                viewing.remove(player);
                player.closeInventory();

                Form.at(player, Prefix.AUCTION, "Closing auction menu!");
                open(player, null);
                return;
            }
        }
        if (event.getCurrentItem().getItemMeta() != null) {
            if (event.getCurrentItem().getItemMeta().getLore() != null) {
                event.getCurrentItem().getItemMeta().getLore().stream().filter(lore -> lore.startsWith(ChatColor.BLACK + "")).forEach(lore -> {
                    try {
                        UUID find = UUID.fromString(lore.replace(ChatColor.BLACK + "", ""));
                        Auction get = AucManager.fromUUID(find);
                        if (get != null) {
                            player.closeInventory();
                            viewing.put(player, get);
                            player.openInventory(createViewInventory(player, get));
                            opened.remove(player);
                        }
                    } catch (Exception ex) {
                    }
                });
            }
        }

        if (event.getCurrentItem().equals(NEXT_PAGE)) {
            //next page
            int at = opened.get(player)+1;
            player.closeInventory();
            player.openInventory(pages.get(at));
            opened.put(player, at);
        } else if (event.getCurrentItem().equals(PREV_PAGE)) {
            //prev page
            player.closeInventory();
            int at = opened.remove(player)-1;
            player.openInventory(pages.get(at));
            opened.put(player, at);
        }
    }

    private Inventory createViewInventory(Player player, Auction get) {
        Inventory inv = Bukkit.createInventory(player, 9, ChatColor.GOLD+"Auctions");
        inv.setItem(0, ItemUtils.build(Material.GOLD_INGOT, "&e&lClick to bid", "Current bid: " + get.getCurrentBid() + " by " +
                (get.getCurrentBidder() == null ? "No one" : get.getCurrentBidder().getName())));
        inv.setItem(1, ItemUtils.build(Material.REDSTONE, "&e&lMinimum Bid: &a$" + get.getMinInterval()));
        inv.setItem(2, ItemUtils.build(Material.WATCH, "&e&lTime left: &f&l" + formatTime(get.getEndingTime() - System.currentTimeMillis())));
        inv.setItem(8, ItemUtils.build(Material.REDSTONE_BLOCK, "&c&lClose Window"));
        return inv;
    }

    private static String formatTime(long millis) {
        millis/=1000;
        if (millis >= 60 && millis < 3600)
            return (millis/60) + " minute(s)";
        if (millis > 3600)
            return (millis/3600) + " hour(s)";

        return (millis + " second(s)");
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
