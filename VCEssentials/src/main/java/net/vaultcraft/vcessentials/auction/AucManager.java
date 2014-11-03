package net.vaultcraft.vcessentials.auction;

import net.vaultcraft.vcessentials.VCEssentials;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashSet;

/**
 * @author Connor Hollasch
 * @since 11/2/2014
 */
public class AucManager {

    private static HashSet<Auction> auctions = new HashSet<>();
    private static AucStore store;

    public static void init() {
        store = new AucStore();
        store.load();
        new AucInv();

        Runnable update = () -> {
            HashSet<Auction> remove = new HashSet<>();

            auctions.stream().filter(auc -> auc.getEndingTime() <= System.currentTimeMillis()).forEach(auc -> {
                auc.end();
                remove.add(auc);
            });

            auctions.removeAll(remove);
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(VCEssentials.getInstance(), update, 20, 20);
    }

    public static AucStore getStore() {
        return store;
    }

    public static void createAuction(Auction auction) {
        auctions.add(auction);

        for (Player player : Bukkit.getOnlinePlayers()) {
            Form.at(player, Prefix.AUCTION, "&e" + auction.getCreator().getName() + Prefix.AUCTION.getChatColor() + " has created an auction!");
            Form.at(player, Prefix.AUCTION, "Type &o\"/auction &e&o" + auction.getCreator().getName() + "&o" + Prefix.AUCTION.getChatColor()+"\"&r" + Prefix.AUCTION.getChatColor()+" to view the auction!");
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1);
        }
    }

    public static void createSilentAuction(Auction auc) {
        auctions.add(auc);
    }

    public static HashSet<Auction> getAuctions() {
        return auctions;
    }

    public static long getMaxCreationDuration(Group group) {
        if (Group.hasPermission(group, Group.WITHER)) {
            return (1000 * 60 * 60 * 24); //1 day
        }

        if (Group.hasPermission(group, Group.SLIME)) {
            return (1000 * 60 * 60); //1 hour
        }

        return (1000 * 60 * 5); //5 minutes
    }

    public static int countPlayerAuctions(OfflinePlayer player) {
        int valid = 0;
        for (Auction auc : auctions) {
            if (auc.getCreator().equals(player))
                ++valid;
        }
        return valid;
    }

    public static int getMaxAuctions(Group highest) {
        if (Group.hasPermission(highest, Group.ENDERDRAGON))
            return 10;
        if (Group.hasPermission(highest, Group.WITHER))
            return 7;
        if (Group.hasPermission(highest, Group.ENDERMAN))
            return 5;
        if (Group.hasPermission(highest, Group.SKELETON))
            return 4;
        if (Group.hasPermission(highest, Group.SLIME))
            return 3;
        if (Group.hasPermission(highest, Group.WOLF))
            return 2;

        return 1;
    }
}
