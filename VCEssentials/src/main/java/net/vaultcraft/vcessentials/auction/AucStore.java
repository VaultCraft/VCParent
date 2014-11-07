package net.vaultcraft.vcessentials.auction;

import com.google.common.collect.Lists;
import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.GsonBuilder;
import net.vaultcraft.vcessentials.VCEssentials;
import net.vaultcraft.vcutils.file.FileController;
import net.vaultcraft.vcutils.item.ItemSerializer;
import net.vaultcraft.vcutils.item.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
            JSONArray auctions = new JSONArray();

            HashSet<Auction> live = AucManager.getAuctions();
            for (Auction auc : live) {
                String creatorUUID = auc.getCreator().getUniqueId().toString();
                String highestBidderUUID = (auc.getCurrentBidder() == null ? null : auc.getCurrentBidder().getUniqueId().toString());
                double bid = auc.getCurrentBid();
                long dueBy = auc.getEndingTime();
                double minBidInterval = auc.getMinInterval();
                ItemStack getSelling = auc.getSellingStack();

                JSONObject aucObj = new JSONObject();
                aucObj.put("creator", creatorUUID);
                aucObj.put("highest-bidder", highestBidderUUID);
                aucObj.put("bid", bid);
                aucObj.put("due-by", dueBy);
                aucObj.put("min-interval", minBidInterval);
                aucObj.put("auction-id", auc.getAuctionId().toString());
                aucObj.put("selling", ItemSerializer.fromStack(getSelling));
                auctions.add(aucObj);
            }
            JSONArray due = new JSONArray();
            for (UUID key : AucManager.getItemsDue().keySet()) {
                JSONObject dueObj = new JSONObject();
                dueObj.put("player", key.toString());
                JSONArray items = AucManager.getItemsDue(key).stream().map(ItemSerializer::fromStack).collect(Collectors.toCollection(() -> new JSONArray()));
                dueObj.put("items", items);
                due.add(dueObj);
            }
            obj.put("items-due", due);
            obj.put("auctions", auctions);

            JSONParser parser = new JSONParser();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            String jsonString = gson.toJson(parser.parse(obj.toJSONString()));

            FileWriter out = new FileWriter(file);
            out.write(jsonString);
            out.flush();
            out.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void load() {
        if (file == null)
            file = new File(VCEssentials.getInstance().getDataFolder().getAbsolutePath(), "auction.json");
        if (!(VCEssentials.getInstance().getDataFolder().exists()))
            VCEssentials.getInstance().getDataFolder().mkdirs();
        if (!(file.exists()))
            VCEssentials.getInstance().saveResource("auction.json", false);

        JSONParser parser = new JSONParser();
        JSONObject data = null;
        try {
            data = (JSONObject) parser.parse(new FileReader(file));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (data.isEmpty())
            return;

        JSONArray all = (JSONArray) data.get("auctions");

        Iterator auctions = all.iterator();
        while (auctions.hasNext()) {
            //load
            JSONObject obj = (JSONObject)auctions.next();
            OfflinePlayer creator = Bukkit.getOfflinePlayer(UUID.fromString(obj.get("creator").toString()));
            OfflinePlayer bidder = obj.get("highest-bidder") == null ? null : Bukkit.getOfflinePlayer(UUID.fromString(obj.get("highest-bidder").toString()));

            double currentBid = Double.parseDouble(obj.get("bid").toString());
            long dueBy = Long.parseLong(obj.get("due-by").toString());
            double minBidInterval = Double.parseDouble(obj.get("min-interval").toString());

            UUID aucUUID = UUID.fromString(obj.get("auction-id").toString());
            ItemStack stack = ItemSerializer.fromString(obj.get("selling").toString());

            Auction auc = new Auction(creator, stack, minBidInterval, aucUUID);
            auc.setEndingDuration(dueBy);
            auc.setCurrentBid(currentBid, bidder);

            AucManager.createSilentAuction(auc);
        }

        JSONArray due = (JSONArray) data.get("items-due");

        Iterator dueIt = due.iterator();
        while (dueIt.hasNext()) {
            JSONObject in = (JSONObject) dueIt.next();
            UUID owe = UUID.fromString(in.get("player").toString());

            JSONArray itemsOwed = (JSONArray)in.get("items");

            itemsOwed.iterator().forEachRemaining((stack) -> AucManager.initDue(owe, ItemSerializer.fromString(stack.toString())));
        }
    }
}
