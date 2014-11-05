package net.vaultcraft.vcessentials.auction;

import net.vaultcraft.vcessentials.VCEssentials;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.string.StringUtils;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import net.vaultcraft.vcutils.util.DateUtil;
import net.vaultcraft.vcutils.util.SignGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @author Connor Hollasch
 * @since 11/2/2014
 */
public class VCAuction extends ICommand {

    public VCAuction(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
        subCmds.put("create", "Creates an auction with the item you're holding");
        subCmds.put("remove", "Used to remove one of your current auctions");
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            //Open the auction GUI
            AucInv.open(player, null);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "help": {
                Form.atHelp(player, this);
                return;
            }
            case "begin":
            case "add":
            case "start":
            case "create": {
                processCreate(player, StringUtils.buildFromArray(args, 1).split(" "));
                return;
            }
            case "toggle":
            case "on":
            case "off":
            case "silent":
            case "ignore":
            case "stfu":
            case "quiet": {
                User user = User.fromPlayer(player);
                if (!user.hasUserdata("auc-silent"))
                    user.addUserdata("auc-silent", ""+false);

                Boolean current = Boolean.valueOf(user.getUserdata("auc-silent"));
                if (current == true) current = false; else current = true;

                user.addUserdata("auc-silent", current.toString());
                if (current) {
                    Form.at(player, Prefix.AUCTION, "You have silenced auction notifications!");
                } else {
                    Form.at(player, Prefix.AUCTION, "You have un-silenced auction notifications!");
                }
                return;
            }
            case "receive":
            case "claim": {
                List<ItemStack> claim = AucManager.getItemsDue(player.getUniqueId());
                if (claim == null || claim.size() == 0) {
                    Form.at(player, Prefix.ERROR, "You do not have any items due!");
                    return;
                }

                int slots = (int)((Math.ceil(claim.size()/9))*9);
                if (slots < 9)
                    slots = 9;

                Inventory inv = Bukkit.createInventory(null, slots, ChatColor.GOLD+"Items Due");
                inv.addItem(claim.toArray(new ItemStack[0]));
                player.openInventory(inv);

                Listener listener = new Listener() {
                    @EventHandler
                    public void onThing(InventoryClickEvent event) {
                        if (event.getWhoClicked().equals(player)) {
                            event.setCancelled(true);

                            if (event.getCurrentItem() == null)
                                return;

                            ItemStack move = event.getCurrentItem();
                            if (move == null || move.getType().equals(Material.AIR) || player.getInventory().equals(event.getInventory()))
                                return;

                            if (player.getInventory().firstEmpty() == -1) {
                                Form.at(player, Prefix.ERROR, "Cannot take out item when your inventory is full!");
                                return;
                            }

                            event.getInventory().removeItem(move);
                            event.setCurrentItem(null);
                            player.getInventory().addItem(move);
                            player.updateInventory();

                            AucManager.removeDue(player.getUniqueId(), move);
                            Form.at(player, Prefix.AUCTION, "You have claimed your &e" + move.getType().toString().toLowerCase() + Prefix.AUCTION.getChatColor()+"!");
                        }
                    }

                    @EventHandler
                    public void onInventoryClose(InventoryCloseEvent event) {
                        AucManager.getStore().save();
                        HandlerList.unregisterAll(this);
                    }
                };
                Bukkit.getPluginManager().registerEvents(listener, VCEssentials.getInstance());
                return;
            }
            default: {
                Player canFind = Bukkit.getPlayer(args[0]);

                if (canFind != null) {
                    AucInv.open(player, canFind);
                    return;
                }

                OfflinePlayer find = Bukkit.getOfflinePlayer(args[0]);
                if (find == null || !find.hasPlayedBefore()) {
                    Form.at(player, Prefix.ERROR, "Cannot find player &e" + args[0] + Prefix.ERROR.getChatColor() + "!");
                    return;
                }

                AucInv.open(player, find);
            }
        }
    }

    private void processCreate(Player player, String[] args) {
        int live = AucManager.countPlayerAuctions(player);
        int max = AucManager.getMaxAuctions(User.fromPlayer(player).getGroup().getHighest());

        if (live >= max) {
            Form.at(player, Prefix.ERROR, "You cannot create more than &e" + max + Prefix.ERROR.getChatColor() + " auctions!");
            return;
        }

        ItemStack holding = player.getItemInHand();

        if (holding == null || holding.getType().equals(Material.AIR)) {
            Form.at(player, Prefix.ERROR, "You cannot auction nothing!");
            return;
        }

        if (AucManager.isPrison && (holding.getType().equals(Material.DIAMOND_PICKAXE) || holding.getType().equals(Material.DIAMOND_SWORD))) {
            Form.at(player, Prefix.ERROR, "You cannot auction this item!");
            return;
        }

        SignGUI.SignGUIListener listener = (player1, lines) -> {
            if (lines[1] == null || lines[1].equals("")) {
                Form.at(player, Prefix.ERROR, "You didn't specify a time for the auction to last!");
                Form.at(player, Prefix.ERROR, "Time Format: y = year, mo = month, d = day, h = hour, m = minute, s = second. ");
                Form.at(player, Prefix.ERROR, "Example: 1h30m = 1 hour 30 minutes.");
                return;
            }

            if (lines[3] == null || lines[3].equals("")) {
                Form.at(player, Prefix.ERROR, "You didn't specify a minimum bid amount!");
                return;
            }

            Date d = new Date(DateUtil.parseDateDiff(lines[1], true));
            long diff = d.getTime() - System.currentTimeMillis();

            long maxDate = AucManager.getMaxCreationDuration(User.fromPlayer(player).getGroup().getHighest());

            if (diff > maxDate) {
                Form.at(player, Prefix.ERROR, "You cannot create an auction for more than " + (maxDate == (1000 * 60 * 5) ? "5 minutes" : (maxDate == (1000 * 60 * 60) ? "1 hour" :  "1 day")) + "!");
                return;
            }

            double minBid = 0.0;
            try {
                minBid = Double.parseDouble(lines[3].replace("$", ""));
                if (minBid < 1.0) {
                    Form.at(player, Prefix.ERROR, "You cannot set the minimum bid less than $1");
                    return;
                }
            } catch (Exception ex) {
                Form.at(player, Prefix.ERROR, "Could not parse the time specified in the sign");
                return;
            }

            Auction auc = new Auction(player, holding, minBid);
            auc.setEndingDuration(System.currentTimeMillis() + diff);
            AucManager.createAuction(auc);
            AucManager.getStore().save();

            player.getInventory().setItemInHand(new ItemStack(Material.AIR));
            player.updateInventory();
        };
        VCUtils.getSignGUI().open(player, new String[]{"Auction time", "(e.g) 1d ", "Min interval", "$"}, listener);
    }
}
