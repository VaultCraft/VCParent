package net.vaultcraft.vcessentials.auction;

import net.vaultcraft.vcessentials.VCEssentials;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * @author Connor Hollasch
 * @since 11/2/2014
 */
public class Auction {

    private OfflinePlayer creator;
    private ItemStack selling;
    private double currentBid;
    private OfflinePlayer currentHolder;
    private double minInterval;
    private long dueBy;

    private UUID auctionId;

    public Auction(OfflinePlayer player, ItemStack stack, double minInterval) {
        this.creator = player;
        this.selling = stack;
        this.minInterval = minInterval;
        this.auctionId = UUID.randomUUID();
    }

    public Auction(OfflinePlayer player, ItemStack stack, double minInterval, UUID uuid) {
        this(player, stack, minInterval);
        this.auctionId = uuid;
    }

    public void setMinInterval(double interval) {
        this.minInterval = interval;
    }

    public void setEndingDuration(long duration) {
        this.dueBy = duration;
    }

    public ItemStack getSellingStack() {
        return selling;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public OfflinePlayer getCurrentBidder() {
        return currentHolder;
    }

    public double getMinInterval() {
        return minInterval;
    }

    public UUID getAuctionId() {
        return auctionId;
    }

    public long getEndingTime() {
        return dueBy;
    }

    public OfflinePlayer getCreator() {
        return creator;
    }

    public void addToCurrent(double bid, OfflinePlayer bidder) {
        this.currentHolder = bidder;
        this.currentBid+=bid;
    }

    public void setCurrentBid(double bid, OfflinePlayer bidder) {
        this.currentHolder = bidder;
        this.currentBid = bid;
    }

    public void end() {
        if (currentHolder == null) {
            if (creator.isOnline()) {
                Form.at(creator.getPlayer(), Prefix.AUCTION, "One of your auctions has ended with no bidders!");
                return;
            }
        }

        if (currentHolder.isOnline()) {
            Player holder = currentHolder.getPlayer();
            if (holder.getInventory().firstEmpty() == -1) {
                Form.at(holder, Prefix.WARNING, "You do not have enough room in your inventory for this item!");
                Form.at(holder, Prefix.WARNING, "This item will be dropped at your location in 10 seconds!");

                Runnable drop = () -> {
                    if (holder.getInventory().firstEmpty() == -1) {
                        holder.getWorld().dropItemNaturally(holder.getEyeLocation(), selling);
                        Form.at(holder, Prefix.WARNING, "Item dropped at your location!");
                        return;
                    }

                    holder.getInventory().addItem(selling);
                    Form.at(holder, Prefix.SUCCESS, "Item added to your inventory!");
                    return;
                };
                Bukkit.getScheduler().scheduleSyncDelayedTask(VCEssentials.getInstance(), drop, 20 * 10);
            }
        }

        AucManager.getStore().save();
    }
}
