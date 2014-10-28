package net.vaultcraft.vcutils.user;

import com.mongodb.DBObject;
import common.network.PacketInSendAll;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.network.MessageClient;
import net.vaultcraft.vcutils.util.BungeeUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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

    private BukkitRunnable runnable;

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
            Object o = dbObject.get(VCUtils.serverName + "-Money");
            moneyOld = (o == null ? 0 : (o instanceof Double ? (Double) o : (Integer) o));
            tokensOld = dbObject.get("Tokens") == null ? 0 : (Integer) dbObject.get("Tokens");
        } else {
            group.merge(Group.COMMON);
            prefix = null;
        }
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                update();
            }
        };
        runnable.runTaskLater(VCUtils.getInstance(), SAVE_DELAY * 20l);
    }

    private void update() {
        userMap.remove(Bukkit.getOfflinePlayer(UUID.fromString(this.playerUUID)));
        Player player = Bukkit.getPlayer(UUID.fromString(this.playerUUID));
        if(player != null) {
            User user = User.fromPlayer(player);
            user.setGroup(group);
            user.setBanned(banned, tempBan);
            user.setMuted(banned, tempBan);
            user.addMoney(money);
            user.addTokens(tokens);
            user.setPrefix(prefix);
        }

        BungeeUtil.serverPlayerList(new ArrayList<>(Bukkit.getOnlinePlayers()).get(0), "ALL", data -> {
            String server = data.readUTF();
            List<String> playerNames = new ArrayList<>(Arrays.asList(data.readUTF().split(", ")));
            if(playerNames.contains(Bukkit.getOfflinePlayer(UUID.fromString(playerUUID)).getName())) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    ObjectOutputStream objOut = new ObjectOutputStream(out);
                    objOut.writeObject(new UpdatedUserData(this));
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
                    double tokensNew = dbObject.get("Tokens") == null ? 0 : (Integer) dbObject.get("Tokens");
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
                    dbObject.put("Tokens", tokensNew);
                    DBObject dbObject1 = VCUtils.getInstance().getMongoDB().query(VCUtils.mongoDBName, "Users", "UUID",playerUUID);
                    if (dbObject1 != null)
                        VCUtils.getInstance().getMongoDB().update(VCUtils.mongoDBName, "Users", dbObject1, dbObject);
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

    public void addMoney(int amount) {
        money += amount;
        runnable.cancel();
        runnable.runTaskLater(VCUtils.getInstance(), SAVE_DELAY * 20l);
    }

    public void addTokens(int amount) {
        tokens += tokens;
        runnable.cancel();
        runnable.runTaskLater(VCUtils.getInstance(), SAVE_DELAY * 20l);
    }

    public void setBanned(boolean banned, Date tempBan) {
        this.banned = banned;
        this.tempBan = tempBan;
        runnable.cancel();
        runnable.runTaskLater(VCUtils.getInstance(), SAVE_DELAY * 20l);
    }

    public void setMuted(boolean muted, Date tempMute) {
        this.muted = muted;
        this.tempMute = tempMute;
        runnable.cancel();
        runnable.runTaskLater(VCUtils.getInstance(), SAVE_DELAY * 20l);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        runnable.cancel();
        runnable.runTaskLater(VCUtils.getInstance(), SAVE_DELAY * 20l);
    }

    public class UpdatedUserData {

        public String playerUUID;
        private String groups;
        private boolean banned;
        private Date tempBan;
        private boolean muted;
        private Date tempMute;

        private double money;
        private int tokens;

        private String prefix;


        public UpdatedUserData(OfflineUser user) {
            playerUUID = user.getPlayerUUID();
            groups = User.groupsToString(user.getGroup());
            banned = user.isBanned();
            tempBan = user.getTempBan();
            muted = user.isMuted();
            tempMute = user.getTempMute();
            money = user.getChangeInMoney();
            tokens = user.getChangeInTokens();
            prefix = user.getPrefix();
        }

        public void updateUser(User user) {
            List<Integer> groupLevels = User.parseGroups(groups);
            user.setGroup(Group.fromPermLevel(groupLevels.get(0)));
            groupLevels.remove(0);
            for(int i : groupLevels) {
                user.getGroup().merge(Group.fromPermLevel(i));
            }
            user.setBanned(banned, tempBan);
            user.setMuted(muted, tempMute);
            user.addMoney(money);
            user.addTokens(tokens);
            user.setPrefix(prefix);
        }
    }
}
