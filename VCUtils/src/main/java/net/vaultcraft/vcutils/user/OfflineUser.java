package net.vaultcraft.vcutils.user;

import com.mongodb.DBObject;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.util.BungeeUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by tacticalsk8er on 10/26/2014.
 */
public class OfflineUser {

    private String playerUUID = "";
    
    private Group.GroupHandler group;

    private boolean banned = false;
    private Date tempBan = null;
    private boolean muted = false;
    private Date tempMute = null;

    private double money = 0;
    private int tokens = 0;

    private String prefix;

    private HashMap<String, String> globalUserdata = new HashMap<>();
    private HashMap<String, String> userdata = new HashMap<>();


    public OfflineUser(OfflinePlayer player) {
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
            userdata = dbObject.get(VCUtils.serverName + "-UserData") == null ? new HashMap<>() : User.parseData(dbObject.get(VCUtils.serverName + "-UserData").toString());
            globalUserdata = dbObject.get("Global-UserData") == null ? new HashMap<>() : User.parseData(dbObject.get("Global-UserData").toString());
        } else {
            group.merge(Group.COMMON);
        }
    }

    public void update() {
        Player player = Bukkit.getPlayer(UUID.fromString(this.playerUUID));
        if(player != null) {
            User user = User.fromPlayer(player);
            user.setGroup(group);
            user.setBanned(banned, tempBan);
            user.setMuted(banned, tempBan);
            user.addMoney(money);
            user.addTokens(tokens);
            user.setPrefix(prefix);
            for(Map.Entry<String, String> entry : userdata.entrySet()) {
                user.addUserdata(entry.getKey(), entry.getValue());
            }
            for(Map.Entry<String, String> entry : globalUserdata.entrySet()) {
                user.addGlobalUserdata(entry.getKey(), entry.getValue());
            }
        }

        BungeeUtil.serverPlayerList(new ArrayList<>(Bukkit.getOnlinePlayers()).get(0), "ALL", data -> {

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
    
    public int getChangeInTokens() {
        return tokens;
    }
    
    public String getUserdata(String key) {
        return userdata.get(key);
    }
    
    public void addUserdata(String key, String value) {
        if(userdata.containsKey(key))
            userdata.remove(key);
        userdata.put(key, value);
    }
    
    public String getGlobalUserdata(String key) {
        return globalUserdata.get(key);
    }

    public void addGlobalUserdata(String key, String value) {
        if(globalUserdata.containsKey(key))
            globalUserdata.remove(key);
        globalUserdata.put(key, value);
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public void addTokens(int amount) {
        tokens += tokens;
    }

    public void setBanned(boolean banned, Date tempBan) {
        this.banned = banned;
        this.tempBan = tempBan;
    }

    public void setMuted(boolean muted, Date tempMute) {
        this.muted = muted;
        this.tempMute = tempMute;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
