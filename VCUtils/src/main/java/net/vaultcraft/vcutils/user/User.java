package net.vaultcraft.vcutils.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.scoreboard.VCScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class User {

    public static volatile ConcurrentHashMap<Player, User> async_player_map = new ConcurrentHashMap<>();

    public static User fromPlayer(Player player) {
        return async_player_map.get(player);
    }

    private Player conversation;
    private boolean editMode;
    private Group group = Group.COMMON;
    private Player player;
    private boolean isChatVisible = true;
    private boolean isPrivateMessaging = true;
    private VCScoreboard scoreboard = null;

    private boolean banned = false;
    private Date tempBan = null;
    private boolean muted = false;
    private Date tempMute = null;

    private double money = 0;
    private int tokens = 0;

    private HashMap<String, String> globalUserdata = new HashMap<>();
    private HashMap<String, String> userdata = new HashMap<>();

    public User(final Player player) {
        this.player = player;
        async_player_map.put(player, User.this);
        Bukkit.getScheduler().runTaskAsynchronously(VCUtils.getInstance(), new Runnable() {
            @Override
            public void run() {
                DBObject dbObject = VCUtils.getInstance().getMongoDB().query("VaultCraft", "Users", "UUID", player.getUniqueId().toString());
                if (dbObject != null) {
                    group = dbObject.get("Group") == null ? Group.COMMON : Group.fromPermLevel((Integer) dbObject.get("Group"));
                    banned = dbObject.get("Banned") == null ? false : (Boolean) dbObject.get("Banned");
                    tempBan = (Date) dbObject.get("TempBan");
                    muted = dbObject.get("Muted") == null ? false : (Boolean) dbObject.get("Muted");
                    tempMute = (Date) dbObject.get("TempMute");

                    Object o = dbObject.get(VCUtils.serverName+"-Money");
                    double value = (o == null ? 0 : (o instanceof Double ? (Double) o : (Integer) o));

                    money = dbObject.get(VCUtils.serverName + "-Money") == null ? 0 : value;
                    tokens = dbObject.get("Tokens") == null? 0 : (Integer) dbObject.get("Tokens");
                    userdata = dbObject.get(VCUtils.serverName + "-UserData") == null ? new HashMap<String, String>() : parseData((String) dbObject.get(VCUtils.serverName + "-UserData"));
                    globalUserdata = dbObject.get("Global-UserData") == null ? new HashMap<String, String>() : parseData((String) dbObject.get("Global-UserData"));
                    //Check if banned
                    Bukkit.getScheduler().runTask(VCUtils.getInstance(), new Runnable() {
                        public void run() {
                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy  HH:mm:ss");
                            UserLoadedEvent event = new UserLoadedEvent(User.this);
                            if (banned) {
                                async_player_map.remove(player, User.this);
                                if (tempBan != null) {
                                    Date now = new Date();
                                    if (now.after(tempBan)) {
                                        setBanned(false, null);
                                        Bukkit.getPluginManager().callEvent(event);
                                        return;
                                    }
                                    player.kickPlayer("You are banned! You can join on " + sdf.format(tempBan));
                                    return;
                                } else {
                                    player.kickPlayer("You are banned!");
                                    return;
                                }
                            }
                            Bukkit.getPluginManager().callEvent(event);
                        }
                    });
                } else {
                    Bukkit.getScheduler().runTask(VCUtils.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            UserLoadedEvent event = new UserLoadedEvent(User.this);
                            VCUtils.getInstance().getServer().getPluginManager().callEvent(event);
                        }
                    });
                }
            }
        });
    }

    public void addUserdata(String key, String value) {
        if (userdata.containsKey(key))
            userdata.remove(key);

        userdata.put(key, value);
    }

    public void addGlobalUserdata(String key, String value) {
        if (userdata.containsKey(key))
            userdata.remove(key);

        userdata.put(key, value);
    }

    public String getUserdata(String key) {
        return userdata.get(key);
    }

    public String getGlobalUserdata(String key) {
        return globalUserdata.get(key);
    }

    public void setChatVisible(boolean visible) {
        this.isChatVisible = visible;
    }

    public boolean isChatVisible() {
        return isChatVisible;
    }

    public void setPrivateMessaging(boolean messaging) {
        this.isPrivateMessaging = messaging;
    }

    public boolean isPrivateMessaging() {
        return isPrivateMessaging;
    }

    public HashMap<String, String> getAllUserdata() {
        return userdata;
    }

    public HashMap<String, String> getAllGlobalUserdata() {
        return globalUserdata;
    }

    public static void remove(final Player player) {
        final User user = async_player_map.get(player);
        Bukkit.getScheduler().runTaskAsynchronously(VCUtils.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(user.getScoreboard() != null)
                    user.getScoreboard().remove();
                DBObject dbObject = VCUtils.getInstance().getMongoDB().query("VaultCraft", "Users", "UUID", player.getUniqueId().toString()) == null ? new BasicDBObject() : VCUtils.getInstance().getMongoDB().query("VaultCraft", "Users", "UUID", player.getUniqueId().toString());
                dbObject.put("UUID", player.getUniqueId().toString());
                dbObject.put("Group", user.getGroup().getPermLevel());
                dbObject.put("Banned", user.isBanned());
                dbObject.put("TempBan", user.getTempBan());
                dbObject.put("Muted", user.isMuted());
                dbObject.put("TempMute", user.getTempMute());
                dbObject.put(VCUtils.serverName + "-Money", user.getMoney());
                dbObject.put("Tokens", user.getTokens());
                dbObject.put(VCUtils.serverName + "-UserData", dataToString(user.userdata));
                dbObject.put("Global-UserData", dataToString(user.globalUserdata));
                DBObject dbObject1 = VCUtils.getInstance().getMongoDB().query("VaultCraft", "Users", "UUID", player.getUniqueId().toString());
                if (dbObject1 == null)
                    VCUtils.getInstance().getMongoDB().insert("VaultCraft", "Users", dbObject);
                else
                    VCUtils.getInstance().getMongoDB().update("VaultCraft", "Users", dbObject1, dbObject);
            }
        });
        async_player_map.remove(player);
    }

    public static void disable() {
        for (User user : async_player_map.values()) {
            if(user.getScoreboard() != null)
                user.getScoreboard().remove();
            DBObject dbObject = VCUtils.getInstance().getMongoDB().query("VaultCraft", "Users", "UUID", user.getPlayer().getUniqueId().toString()) == null ? new BasicDBObject() : VCUtils.getInstance().getMongoDB().query("VaultCraft", "Users", "UUID", user.getPlayer().getUniqueId().toString());
            dbObject.put("UUID", user.getPlayer().getUniqueId().toString());
            dbObject.put("Group", user.getGroup().getPermLevel());
            dbObject.put("Banned", user.isBanned());
            dbObject.put("TempBan", user.getTempBan());
            dbObject.put("Muted", user.isMuted());
            dbObject.put("TempMute", user.getTempMute());
            dbObject.put(VCUtils.serverName + "-Money", user.getMoney());
            dbObject.put("Tokens", user.getTokens());
            dbObject.put(VCUtils.serverName + "-UserData", dataToString(user.userdata));
            dbObject.put("Global-UserData", dataToString(user.globalUserdata));
            DBObject dbObject1 = VCUtils.getInstance().getMongoDB().query("VaultCraft", "Users", "UUID", user.getPlayer().getUniqueId().toString());
            if (dbObject1 == null)
                VCUtils.getInstance().getMongoDB().insert("VaultCraft", "Users", dbObject);
            else
                VCUtils.getInstance().getMongoDB().update("VaultCraft", "Users", dbObject1, dbObject);
        }
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public Player getPlayer() {
        return player;
    }

    public Player modifyConversation(Player other) {
        if (other == null)
            return conversation;

        this.conversation = other;
        return other;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setBanned(boolean banned, Date tempBan) {
        this.banned = banned;
        this.tempBan = tempBan;
    }

    public void setMuted(boolean muted, Date tempMute) {
        this.muted = muted;
        this.tempMute = tempMute;
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

    public VCScoreboard getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(VCScoreboard scoreboard) {
        this.scoreboard = scoreboard;
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
        if(data.contains(",")) {
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
}