package net.vaultcraft.vcessentials.auction;

import net.vaultcraft.vcessentials.VCEssentials;
import net.vaultcraft.vcutils.file.FileController;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Connor Hollasch
 * @since 11/2/2014
 */
public class AucStore implements FileController {

    private static File file = new File(VCEssentials.getInstance().getDataFolder().getAbsolutePath(), "auction.json");

    public File getFile() {
        return file;
    }

    public void save() {
        try {
            JSONObject obj = new JSONObject();
            JSONArray regions = new JSONArray();

            HashSet<Auction> live = AucManager.getAuctions();
            for (Auction auc : live) {
                String creatorUUID = auc.getCreator().getUniqueId().toString();
                String highestBidderUUID = auc.getCurrentBidder().getUniqueId().toString();
                double bid = auc.getCurrentBid();
                long dueBy = auc.getEndingTime();
                double minBidInterval = auc.getMinInterval();
                ItemStack getSelling = auc.getSellingStack();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void load() {

    }
}
