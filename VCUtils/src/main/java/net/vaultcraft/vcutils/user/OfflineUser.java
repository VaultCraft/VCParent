package net.vaultcraft.vcutils.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tacticalsk8er on 8/14/2014.
 */
public class OfflineUser {

    public static volatile ConcurrentHashMap<OfflinePlayer, OfflineUser> async_player_map = new ConcurrentHashMap<>();

    public static OfflineUser fromPlayer(OfflinePlayer offlinePlayer) {
        return async_player_map.get(offlinePlayer);
    }

    private OfflinePlayer offlinePlayer;
    private Group group = Group.COMMON;
    private boolean banned = false;
    private Date tempBan = null;
    private boolean muted = false;
    private Date tempMute = null;

    private double money = 0;
    private int tokens = 0;

    private HashMap<String, String> globalUserdata = new HashMap<>();
    private HashMap<String, String> userdata = new HashMap<>();

    private List<UpdateUser> updates = new ArrayList<>();

    public OfflineUser(final OfflinePlayer offlinePlayer, UpdateUser updateUser) {
        this.offlinePlayer = offlinePlayer;
        async_player_map.put(offlinePlayer, OfflineUser.this);
        updates.add(updateUser);
        Bukkit.getScheduler().runTaskAsynchronously(VCUtils.getInstance(), new Runnable() {
            @Override
            public void run() {
                DBObject dbObject;
                try {
                    dbObject = VCUtils.getInstance().getMongoDB().query("VaultCraft", "Users", "UUID", UUIDFetcher.getUUIDOf(offlinePlayer.getName()).toString());
                } catch (Exception e) {
                    Logger.error(VCUtils.getInstance(), e);
                    async_player_map.remove(offlinePlayer);
                    return;
                }
                if (dbObject != null) {
                    group = dbObject.get("Group") == null ? Group.COMMON : Group.fromPermLevel((Integer) dbObject.get("Group"));
                    banned = dbObject.get("Banned") == null ? false : (Boolean) dbObject.get("Banned");
                    tempBan = (Date) dbObject.get("TempBan");
                    muted = dbObject.get("Muted") == null ? false : (Boolean) dbObject.get("Muted");
                    tempMute = (Date) dbObject.get("TempMute");

                    Object o = dbObject.get(VCUtils.serverName + "-Money");
                    double value = (o == null ? 0 : (o instanceof Double ? (Double) o : (Integer) o));

                    money = dbObject.get(VCUtils.serverName + "-Money") == null ? 0 : value;
                    tokens = dbObject.get("Tokens") == null ? 0 : (Integer) dbObject.get("Tokens");
                    userdata = dbObject.get(VCUtils.serverName + "-UserData") == null ? new HashMap<String, String>() : parseData((String) dbObject.get(VCUtils.serverName + "-UserData"));
                    globalUserdata = dbObject.get("Global-UserData") == null ? new HashMap<String, String>() : parseData((String) dbObject.get("Global-UserData"));
                }
                new Updater(20 * 10, OfflineUser.this).runTaskLaterAsynchronously(VCUtils.getInstance(), 1l);
            }
        });
    }

    public Group getGroup() {
        return group;
    }

    public OfflinePlayer getOfflinePlayer() {
        return offlinePlayer;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned, Date tempBan) {
        this.banned = banned;
        this.tempBan = tempBan;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted, Date tempMute) {
        this.muted = muted;
        this.tempMute = tempMute;
    }

    public Date getTempBan() {
        return tempBan;
    }

    public Date getTempMute() {
        return tempMute;
    }

    public double getMoney() {
        return money;
    }

    public int getTokens() {
        return tokens;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public void addMoney(double addTo) {
        money += addTo;
    }

    public void addTokens(int addTo) {
        tokens += addTo;
    }

    private static String dataToString(HashMap<String, String> userdata) {
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        for (Map.Entry entry : userdata.entrySet()) {
            sb.append(entry.getKey()).append(":").append(entry.getValue());
            if (userdata.size() - 1 != counter)
                sb.append(",");
            counter++;
        }
        return sb.toString();
    }

    private static HashMap<String, String> parseData(String data) {
        HashMap<String, String> userdata = new HashMap<>();
        if (!(data.contains(":")))
            return userdata;
        if (data.contains(",")) {
            String[] parts = data.split(",");
            for (String s : parts) {
                String[] entry = s.split(":");
                userdata.put(entry[0], entry[1]);
            }
        } else {
            String[] parts = data.split(":");
            userdata.put(parts[0], parts[1]);
        }
        return userdata;
    }

    public void addUpdate(UpdateUser updateUser) {
        updates.add(updateUser);
    }

    public interface UpdateUser {
        public void update(OfflineUser offlineUser);
    }

    private class Updater extends BukkitRunnable {

        int reset;
        int ticks;
        OfflineUser offlineUser;

        public Updater(int ticks, OfflineUser offlineUser) {
            this.reset = ticks;
            this.ticks = ticks;
            this.offlineUser = offlineUser;
        }

        @Override
        public void run() {
            if(ticks == 0) {
                offlineUser.done();
                return;
            }
            if(updates.size() == 0) {
                ticks --;
                this.runTaskLaterAsynchronously(VCUtils.getInstance(), 1l);
                return;
            }
            for(UpdateUser updateUser : updates) {
                updateUser.update(offlineUser);
            }
            updates.clear();
            ticks = reset;
            this.runTaskLaterAsynchronously(VCUtils.getInstance(), 1l);
        }
    }

    public void done() {
        async_player_map.remove(this.getOfflinePlayer());
        updates.clear();
        String UUID;
        try {
            UUID = UUIDFetcher.getUUIDOf(this.getOfflinePlayer().getName()).toString();
        } catch (Exception e) {
            Logger.error(VCUtils.getInstance(), e);
            return;
        }
        DBObject dbObject = VCUtils.getInstance().getMongoDB().query("VaultCraft", "Users", "UUID", UUID) == null ? new BasicDBObject() : VCUtils.getInstance().getMongoDB().query("VaultCraft", "Users", "UUID", UUID);
        dbObject.put("UUID", UUID);
        dbObject.put("Group", this.getGroup().getPermLevel());
        dbObject.put("Banned", this.isBanned());
        dbObject.put("TempBan", this.getTempBan());
        dbObject.put("Muted", this.isMuted());
        dbObject.put("TempMute", this.getTempMute());
        dbObject.put(VCUtils.serverName + "-Money", this.getMoney());
        dbObject.put("Tokens", this.getTokens());
        dbObject.put(VCUtils.serverName + "-UserData", dataToString(this.userdata));
        dbObject.put("Global-UserData", dataToString(this.globalUserdata));
        DBObject dbObject1 = VCUtils.getInstance().getMongoDB().query("VaultCraft", "Users", "UUID", UUID);
        if (dbObject1 == null)
            VCUtils.getInstance().getMongoDB().insert("VaultCraft", "Users", dbObject);
        else
            VCUtils.getInstance().getMongoDB().update("VaultCraft", "Users", dbObject1, dbObject);
    }
}
