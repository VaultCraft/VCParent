package net.vaultcraft.vcessentials.chestshop;

import net.vaultcraft.vcessentials.VCEssentials;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.user.OfflineUser;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Sign;

import java.util.HashMap;

/**
 * Created by tacticalsk8er on 10/29/2014.
 */
public class ShopListener implements Listener {


    public ShopListener() {
        Bukkit.getPluginManager().registerEvents(this, VCEssentials.getInstance());
    }

    @EventHandler
    public void onSignPlace(SignChangeEvent event) {
        Sign sign = (Sign) event.getBlock().getState().getData();
        int index = 0;
        if (!event.getLine(0).equalsIgnoreCase("[Buy]") && !event.getLine(0).equalsIgnoreCase("[Sell]")) {
            index = 1;
            if (!event.getLine(1).equalsIgnoreCase("[Buy]") && !event.getLine(1).equalsIgnoreCase("[Sell]")) {
                index = 2;
                if (!event.getLine(2).equalsIgnoreCase("[Buy]") && !event.getLine(2).equalsIgnoreCase("[Sell]")) {
                    return;
                }
            }
        }

        Block attachedBlock = event.getBlock().getRelative(sign.getAttachedFace());

        if (!attachedBlock.getType().equals(Material.CHEST) && !attachedBlock.getType().equals(Material.TRAPPED_CHEST)) {
            Form.at(event.getPlayer(), Prefix.ERROR, "A shop sign needs to be placed on a chest.");
            event.getBlock().breakNaturally();
            return;
        }

        String sellOrBuy = event.getLine(index);
        String price = event.getLine(index + 1);
        String[] priceParts = price.split(" ");

        if (priceParts.length > 2) {
            Form.at(event.getPlayer(), Prefix.ERROR, "Invalid price! Example: 1234.34, 145.56 mil, 45.567 bil");
            event.getBlock().breakNaturally();
            return;
        }

        double priceD = 0.0;

        if (priceParts.length > 0) {
            try {
                priceD = Double.parseDouble(priceParts[0]);
            } catch (NumberFormatException e) {
                Form.at(event.getPlayer(), Prefix.ERROR, "Invalid price number! Example: 14323.34");
                event.getBlock().breakNaturally();
                return;
            }
        }

        if (priceParts.length == 2) {
            if (!priceParts[1].equalsIgnoreCase("mil")) {
                Form.at(event.getPlayer(), Prefix.ERROR, "Invalid price modifier! Example: mil.");
                event.getBlock().breakNaturally();
                return;
            }
            priceD *= 1000000;
        }

        if(priceD > 15000000) {
            Form.at(event.getPlayer(), Prefix.ERROR, "Price can not be higher than 15,000,000.");
            event.getBlock().breakNaturally();
            return;
        }

        Chest chest = (Chest) attachedBlock.getState();
        ItemStack item = chest.getInventory().getItem(0);

        if (item == null) {
            Form.at(event.getPlayer(), Prefix.ERROR, "Sell item missing from the chest! Place the item and the " +
                    "amount of the item you want to sell in the upper left hand corner.");
            event.getBlock().breakNaturally();
            return;
        }

        event.setLine(0, event.getPlayer().getName());
        if (sellOrBuy.toLowerCase().contains("buy"))
            event.setLine(1, ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "[Buy]");
        else
            event.setLine(1, ChatColor.WHITE.toString() + ChatColor.BOLD + "[Sell]");
        event.setLine(2, ChatColor.GREEN + "$" + price);
        event.setLine(3, "ID:" + item.getType().getId() + ":" + item.getDurability() + "@" + item.getAmount());

        Form.at(event.getPlayer(), Prefix.SUCCESS, "Shop has been created!");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null)
            return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;
        if (!event.getClickedBlock().getType().equals(Material.WALL_SIGN))
            return;

        event.setCancelled(true);

        org.bukkit.block.Sign sign = (org.bukkit.block.Sign) event.getClickedBlock().getState();
        Sign signData = (Sign) sign.getData();
        if(!event.getClickedBlock().getRelative(signData.getAttachedFace()).getType().equals(Material.CHEST) && !event.getClickedBlock().getRelative(signData.getAttachedFace()).getType().equals(Material.TRAPPED_CHEST))
            return;
        Chest chest = (Chest) event.getClickedBlock().getRelative(signData.getAttachedFace()).getState();

        if (!ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase("[buy]") && !ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase("[sell]"))
            return;
        if (ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase("[buy]")) {
            String[] priceParts = ChatColor.stripColor(sign.getLine(2)).replace("$", "").split(" ");
            double price = 0;
            if (priceParts.length > 0)
                price = Double.parseDouble(priceParts[0]);

            if (priceParts.length > 1) {
                if (priceParts[1].equalsIgnoreCase("mil"))
                    price *= 1000000;
            }

            User user = User.fromPlayer(event.getPlayer());

            if (user.getMoney() < price) {
                Form.at(event.getPlayer(), Prefix.ERROR, "You don't have enough money to purchase.");
                return;
            }

            int[] parts = getParts(sign.getLine(3));
            ItemStack itemStack = new ItemStack(Material.getMaterial(parts[0]), parts[2], (short)parts[1]);
            ItemStack singleton = itemStack.clone();
            singleton.setAmount(1);

            boolean contains = false;
            for (ItemStack i : chest.getInventory().getContents()) {
                if (i == null)
                    continue;

                ItemStack clone = i.clone();
                clone.setAmount(1);

                if (singleton.equals(clone) && i.getAmount() >= itemStack.getAmount()) {
                    contains = true;
                    break;
                }
            }

            if (!contains) {
                Form.at(event.getPlayer(), Prefix.ERROR, "Chest does not contain the selling item.");
                return;
            }

            chest.getInventory().removeItem(itemStack);
            HashMap<Integer, ItemStack> lostItems = event.getPlayer().getInventory().addItem(itemStack);

            chest.update();

            if (!lostItems.isEmpty()) {
                for (ItemStack stack : lostItems.values())
                    event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), stack);
                Form.at(event.getPlayer(), Prefix.WARNING, "You did not have enough room in your inventory. Items will appear next to you.");
            }

            user.addMoney(-price);

            Form.at(event.getPlayer(), Prefix.SUCCESS, "You bought " + itemStack.getAmount() + " " + itemStack.getType().name() + " from " + sign.getLine(0));

            Player player = Bukkit.getPlayer(sign.getLine(0));
            if (player == null) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(sign.getLine(0));
                if (offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                    OfflineUser user1 = OfflineUser.getOfflineUser(offlinePlayer);
                    user1.addMoney(price);
                    return;
                }
                return;
            }

            User user1 = User.fromPlayer(player);
            user1.addMoney(price);
            event.getPlayer().updateInventory();
        } else {
            String[] priceParts = ChatColor.stripColor(sign.getLine(2)).replace("$", "").split(" ");
            double price = 0;
            if (priceParts.length > 0)
                price = Double.parseDouble(priceParts[0]);

            if (priceParts.length > 1) {
                if (priceParts[1].equalsIgnoreCase("mil"))
                    price *= 1000000;
            }

            int[] parts = getParts(sign.getLine(3));
            ItemStack itemStack = new ItemStack(Material.getMaterial(parts[0]), parts[2], (byte)parts[1]);
            ItemStack singleton = itemStack.clone();
            singleton.setAmount(1);

            boolean contains = false;
            for (ItemStack i : event.getPlayer().getInventory().getContents()) {
                if (i == null)
                    continue;

                ItemStack clone = i.clone();
                clone.setAmount(1);

                if (singleton.equals(clone) && i.getAmount() >= itemStack.getAmount()) {
                    contains = true;
                    break;
                }
            }

            if (!contains) {
                Form.at(event.getPlayer(), Prefix.ERROR, "You don't have the buying item.");
                return;
            }

            if (chest.getInventory().firstEmpty() == -1) {
                Form.at(event.getPlayer(), Prefix.ERROR, "The chest doesn't have enough room!");
                return;
            }

            Player player = Bukkit.getPlayer(sign.getLine(0));

            if (player == null) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(sign.getLine(0));

                if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
                    Form.at(event.getPlayer(), Prefix.ERROR, "Something went wrong...");
                    return;
                }

                OfflineUser user = OfflineUser.getOfflineUser(offlinePlayer);
                if ((user.getMoneyOld() + user.getChangeInMoney()) < price) {
                    Form.at(event.getPlayer(), Prefix.ERROR, offlinePlayer.getName() + " doesn't have enough money to pay you.");
                    return;
                }
                user.addMoney(-price);
            } else {
                User user = User.fromPlayer(player);
                if (user.getMoney() < price) {
                    Form.at(event.getPlayer(), Prefix.ERROR, player.getName() + " doesn't have enough money to pay you.");
                    return;
                }
                user.addMoney(-price);
            }

            event.getPlayer().getInventory().removeItem(itemStack);
            chest.getInventory().addItem(itemStack);

            event.getPlayer().updateInventory();
            chest.update();

            User.fromPlayer(event.getPlayer()).addMoney(price);
            Form.at(event.getPlayer(), Prefix.SUCCESS, "You sold " + getParts(sign.getLine(3))[2] + " " + itemStack.getType().name() + " to " + sign.getLine(0));
        }
    }

    private static int[] getParts(String x) {
        String[] splitAt = x.split("@");
        String[] splitColon = splitAt[0].split(":");

        int id = Integer.parseInt(splitColon[1]);
        byte data = (byte)Integer.parseInt(splitColon[2]);

        int amount = Integer.parseInt(splitAt[1]);

        return new int[] {id, data, amount};
    }
}
