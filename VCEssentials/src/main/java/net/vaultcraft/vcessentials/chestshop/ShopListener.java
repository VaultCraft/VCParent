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
        if(!event.getLine(0).equalsIgnoreCase("[Buy]") && !event.getLine(0).equalsIgnoreCase("[Sell]")) {
            index = 1;
            if (!event.getLine(1).equalsIgnoreCase("[Buy]") && !event.getLine(1).equalsIgnoreCase("[Sell]")) {
                index = 2;
                if (!event.getLine(2).equalsIgnoreCase("[Buy]") && !event.getLine(2).equalsIgnoreCase("[Sell]")) {
                    return;
                }
            }
        }

        Block attachedBlock = event.getBlock().getRelative(sign.getAttachedFace());

        if(!attachedBlock.getType().equals(Material.CHEST)) {
            Form.at(event.getPlayer(), Prefix.ERROR, "A shop sign needs to be placed on a chest.");
            event.getBlock().breakNaturally();
            return;
        }

        String sellOrBuy = event.getLine(index);
        String price = event.getLine(index + 1);
        String[] priceParts = price.split(" ");

        if(priceParts.length > 2) {
            Form.at(event.getPlayer(), Prefix.ERROR, "Invalid price! Example: 1234.34, 145.56 mil, 45.567 bil");
            event.getBlock().breakNaturally();
            return;
        }

        if(priceParts.length > 0) {
            try {
                Double.parseDouble(priceParts[0]);
            } catch (NumberFormatException e) {
                Form.at(event.getPlayer(), Prefix.ERROR, "Invalid price number! Example: 14323.34");
                event.getBlock().breakNaturally();
                return;
            }
        }

        if(priceParts.length == 2) {
            if(!priceParts[1].equalsIgnoreCase("mil") || !priceParts[1].equalsIgnoreCase("bil")) {
                Form.at(event.getPlayer(), Prefix.ERROR, "Invalid price modifier! Example: mil or bil.");
                event.getBlock().breakNaturally();
                return;
            }
        }

        Chest chest = (Chest) attachedBlock.getState();
        ItemStack item = chest.getInventory().getItem(0);

        if(item == null) {
            Form.at(event.getPlayer(), Prefix.ERROR, "Sell item missing from the chest! Place the item and the " +
                    "amount of the item you want to sell in the upper right hand corner.");
            event.getBlock().breakNaturally();
            return;
        }

        event.setLine(0, event.getPlayer().getName());
        event.setLine(1, ChatColor.BLUE + sellOrBuy);
        event.setLine(2, ChatColor.GREEN + price);
        event.setLine(3, item.getType().getId() + ":" + item.getDurability() + " " + item.getAmount());

        Form.at(event.getPlayer(), Prefix.SUCCESS, "Shop has been created!");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if(event.getClickedBlock() == null)
            return;
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;
        if(!event.getClickedBlock().getType().equals(Material.WALL_SIGN))
            return;

        org.bukkit.block.Sign sign = (org.bukkit.block.Sign) event.getClickedBlock().getState();
        Sign signData = (Sign) sign.getData();
        Chest chest = (Chest) event.getClickedBlock().getRelative(signData.getAttachedFace()).getState();

        if(!sign.getLine(1).equalsIgnoreCase(ChatColor.BLUE + "[Buy]") && !sign.getLine(1).equalsIgnoreCase(ChatColor.BLUE + "[Sell]"))
            return;

        if(sign.getLine(1).equalsIgnoreCase(ChatColor.BLUE + "[Buy]")) {

            String[] priceParts = sign.getLine(2).split(" ");
            double price = 0;
            if(priceParts.length > 0)
                price = Double.parseDouble(priceParts[0]);

            if(priceParts.length > 1) {
                if(priceParts[1].equalsIgnoreCase("mil"))
                    price *= 1000000;
                else if(priceParts[1].equalsIgnoreCase("bil"))
                    price *= 100000000;
            }

            User user = User.fromPlayer(event.getPlayer());

            if(user.getMoney() < price)  {
                Form.at(event.getPlayer(), Prefix.ERROR, "You don't have enough money to purchase.");
                return;
            }

            String[] itemParts = sign.getLine(3).split(" ");
            String[] itemData = itemParts[0].split(":");

            ItemStack itemStack = new ItemStack(Material.getMaterial(Integer.parseInt(itemData[0])), Integer.parseInt(itemParts[2]), Short.parseShort(itemData[1]));

            if(!chest.getInventory().contains(itemStack)) {
                Form.at(event.getPlayer(), Prefix.ERROR, "Chest does not contain the selling item.");
                return;
            }

            chest.getInventory().remove(itemStack);
            HashMap<Integer, ItemStack> lostItems = event.getPlayer().getInventory().addItem(itemStack);

            if(!lostItems.isEmpty()) {
                for (ItemStack stack : lostItems.values())
                    event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), stack);
                Form.at(event.getPlayer(), Prefix.WARNING, "You did not have enough room in your inventory. Items will appear next to you.");
            }

            user.addMoney(-price);

            Form.at(event.getPlayer(), Prefix.SUCCESS, "You bought " + itemStack.getAmount() + " " + itemStack.getType().name() + " from " + sign.getLine(0));

            Player player = Bukkit.getPlayer(sign.getLine(0));
            if(player == null) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(sign.getLine(0));
                if(offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                    OfflineUser user1 = OfflineUser.getOfflineUser(offlinePlayer);
                    user1.addMoney(price);
                    return;
                }
                return;
            }

            User user1 = User.fromPlayer(player);
            user1.addMoney(price);
        } else {

            String[] priceParts = sign.getLine(2).split(" ");
            double price = 0;
            if(priceParts.length > 0)
                price = Double.parseDouble(priceParts[0]);

            if(priceParts.length > 1) {
                if(priceParts[1].equalsIgnoreCase("mil"))
                    price *= 1000000;
                else if(priceParts[1].equalsIgnoreCase("bil"))
                    price *= 100000000;
            }

            String[] itemParts = sign.getLine(3).split(" ");
            String[] itemData = itemParts[0].split(":");

            ItemStack itemStack = new ItemStack(Material.getMaterial(Integer.parseInt(itemData[0])), Integer.parseInt(itemParts[2]), Short.parseShort(itemData[1]));

            if(!event.getPlayer().getInventory().contains(itemStack)) {
                Form.at(event.getPlayer(), Prefix.ERROR, "You don't have the buying item.");
                return;
            }

            if(chest.getInventory().firstEmpty() == -1) {
                Form.at(event.getPlayer(), Prefix.ERROR, "The chest doesn't have enough room!");
                return;
            }

            Player player = Bukkit.getPlayer(sign.getLine(0));

            if(player == null) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(sign.getLine(0));

                if(offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
                    Form.at(event.getPlayer(), Prefix.ERROR, "Something went wrong...");
                    return;
                }

                OfflineUser user = OfflineUser.getOfflineUser(offlinePlayer);
                if(user.getMoneyOld() < price) {
                    Form.at(event.getPlayer(), Prefix.ERROR, offlinePlayer.getName() + " doesn't have enough money to pay you.");
                    return;
                }
                user.addMoney(-price);
            } else {
                User user = User.fromPlayer(player);
                if(user.getMoney() < price) {
                    Form.at(event.getPlayer(), Prefix.ERROR, player.getName() + " doesn't have enough money to pay you.");
                    return;
                }
                user.addMoney(-price);
            }

            event.getPlayer().getInventory().remove(itemStack);
            chest.getInventory().addItem(itemStack);

            User.fromPlayer(event.getPlayer()).addMoney(price);
            Form.at(event.getPlayer(), Prefix.SUCCESS, "You sold " + itemStack.getAmount() + " " + itemStack.getType().name() + " to " + sign.getLine(0));
        }
    }
}
