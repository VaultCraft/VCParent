package net.vaultcraft.vcutils.user;

import com.mongodb.DBObject;
import common.network.PacketInSendAll;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.network.MessageClient;
import net.vaultcraft.vcutils.util.BungeeUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * Created by tacticalsk8er on 10/26/2014.
 */
public class OfflineUser {
    
    private static final int SAVE_DELAY = 15; 

    private static HashMap<OfflinePlayer, OfflineUser> userMap = new HashMap<>();

    private String playerUUID = "";
    
    private Group.GroupHandler group;

    private boolean banned = false;
    private Date tempBan = null;
    private boolean muted = false;
    private Date tempMute = null;

    private double moneyOld = 0;
    private int tokensOld = 0;

    private double money = 0;
    private int tokens = 0;

    private String prefix;
    private String nick;

    private BukkitTask task;

    public static OfflineUser getOfflineUser(OfflinePlayer player) {
        if(userMap.containsKey(player)) {
            return userMap.get(player);
        }
        OfflineUser user = new OfflineUser(player);
        userMap.put(player, user);
        return user;
    }

    private OfflineUser(OfflinePlayer player) {
        playerUUID = player.getUniqueId().toString();
        group = new Group.GroupHandler(player.getPlayer());
        DBObject dbObject = VCUtils.getInstance().getMongoDB().query(VCUtils.mongoDBName, "Users", "UUID", player.getUniqueId().toString());
        if (dbObject != null) {
            String groupList = dbObject.get("Group") == null ? "1" : dbObject.get("Group").toString();
            for (int i : User.parseGroups(groupList))
                group.merge(Group.fromPermLevel(i));
            banned = dbObject.get("Banned") == null ? false : (Boolean) dbObject.get("Banned");
            tempBan = (Date) dbObject.get("TempBan");
            muted = dbObject.get("Muted") == null ? false : (Boolean) dbObject.get("Muted");
            tempMute = (Date) dbObject.get("TempMute");
            prefix = dbObject.get("Prefix") == null ? null : dbObject.get("Prefix").toString();
            nick = dbObject.get("Nick") == null ? null : dbObject.get("Nick").toString();
            Object o = dbObject.get(VCUtils.serverName + "-Money");
            moneyOld = (o == null ? 0 : (o instanceof Double ? (Double) o : (Integer) o));
            tokensOld = dbObject.get("Tokens") == null ? 0 : (Integer) dbObject.get("Tokens");
        } else {
            group.merge(Group.COMMON);
            prefix = null;
        }
        task = Bukkit.getScheduler().runTaskLater(VCUtils.getInstance(), this::update, SAVE_DELAY * 20l);
    }

    private void update() {
        userMap.remove(Bukkit.getOfflinePlayer(UUID.fromString(this.playerUUID)));
        Player player = Bukkit.getPlayer(UUID.fromString(this.playerUUID));
        if(player != null) {
            User user = User.fromPlayer(player);
            user.setGroup(group);
            user.setBanned(banned, tempBan);
            if(banned)
                player.kickPlayer("You have been banned! You can post an appeal on our forums.");
            user.setMuted(banned, tempBan);
            user.addMoney(money);
            user.addTokens(tokens);
            user.setPrefix(prefix);
            return;
        }

        BungeeUtil.serverPlayerList(new ArrayList<>(Bukkit.getOnlinePlayers()).get(0), "ALL", data -> {
            String server = data.readUTF();
            List<String> playerNames = new ArrayList<>(Arrays.asList(data.readUTF().split(", ")));
            if(playerNames.contains(Bukkit.getOfflinePlayer(UUID.fromString(playerUUID)).getName())) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    ObjectOutputStream objOut = new ObjectOutputStream(out);
                    objOut.writeObject(new UpdatedUserData(this, VCUtils.serverName));
                    objOut.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PacketInSendAll packet = new PacketInSendAll("update-user", out);
                MessageClient.sendPacket(packet);
            } else {
                DBObject dbObject = VCUtils.getInstance().getMongoDB().query(VCUtils.mongoDBName, "Users", "UUID", playerUUID);
                if(dbObject != null) {
                    //Get old money and token values
                    Object o = dbObject.get(VCUtils.serverName + "-Money");
                    double moneyNew = (o == null ? 0 : (o instanceof Double ? (Double) o : (Integer) o));
                    int tokensNew = dbObject.get("Tokens") == null ? 0 : (Integer) dbObject.get("Tokens");
                    String userdata = dbObject.get(VCUtils.serverName + "-UserData") == null ? "" : dbObject.get(VCUtils.serverName + "-UserData").toString();
                    String globalUserdata = dbObject.get("Global-UserData") == null ? "" : dbObject.get("Global-UserData").toString();

                    moneyNew += money;
                    tokensNew += tokens;

                    //Update Mongo
                    dbObject.put("UUID", playerUUID);
                    dbObject.put("Group", User.groupsToString(group));
                    dbObject.put("Banned", banned);
                    dbObject.put("TempBan", tempBan);
                    dbObject.put("Muted", muted);
                    dbObject.put("TempMute", tempMute);
                    dbObject.put("Prefix", prefix);
                    dbObject.put(VCUtils.serverName + "-Money", moneyNew);
                    dbObject.put(VCUtils.serverName + "-UserData", userdata);
                    dbObject.put("Tokens", tokensNew);
                    dbObject.put("Global-UserData", globalUserdata);
                    DBObject dbObject1 = VCUtils.getInstance().getMongoDB().query(VCUtils.mongoDBName, "Users", "UUID", playerUUID);
                    if (dbObject1 != null) {
                        VCUtils.getInstance().getMongoDB().update(VCUtils.mongoDBName, "Users", dbObject1, dbObject);
                    }
                }
            }
        });
    }

    public String getPlayerUUID() {
        return playerUUID;

    }

    public Group.GroupHandler getGroup() {
        return group;
    }

    public boolean isBanned() {
        return banned;
    }

    public boolean isMuted() {
        return muted;
    }

    public Date getTempBan() {
        return tempBan;
    }

    public Date getTempMute() {
        return tempMute;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getNick() {
        return nick;
    }

    public double getChangeInMoney() {
        return money;
    }

    public double getMoneyOld() {
        return moneyOld;
    }

    public int getChangeInTokens() {
        return tokens;
    }

    public int getTokensOld() {
        return tokensOld;
    }

    public void addMoney(double amount) {
        money += amount;
        task.cancel();
        task = Bukkit.getScheduler().runTaskLater(VCUtils.getInstance(), this::update, SAVE_DELAY * 20l);
    }

    public void addTokens(int amount) {
        tokens += amount;
        task.cancel();
        task = Bukkit.getScheduler().runTaskLater(VCUtils.getInstance(), this::update, SAVE_DELAY * 20l);
    }

    public void setBanned(boolean banned, Date tempBan) {
        this.banned = banned;
        this.tempBan = tempBan;
        task.cancel();
        task = Bukkit.getScheduler().runTaskLater(VCUtils.getInstance(), this::update, SAVE_DELAY * 20l);
    }

    public void setMuted(boolean muted, Date tempMute) {
        this.muted = muted;
        this.tempMute = tempMute;
        task.cancel();
        task = Bukkit.getScheduler().runTaskLater(VCUtils.getInstance(), this::update, SAVE_DELAY * 20l);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        task.cancel();
        task = Bukkit.getScheduler().runTaskLater(VCUtils.getInstance(), this::update, SAVE_DELAY * 20l);
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
