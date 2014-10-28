package net.vaultcraft.vcutils.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.scoreboard.VCScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class User {

    public static volatile ConcurrentHashMap<Player, User> async_player_map = new ConcurrentHashMap<>();
    public static volatile ConcurrentHashMap<String, User> async_uuid_map = new ConcurrentHashMap<>();

    public static User fromPlayer(Player player) {
        return async_player_map.get(player);
    }

    public static User fromUUID(String uuid) {
        return async_uuid_map.get(uuid);
    }

    private Player conversation;
    private boolean editMode;
    private Group.GroupHandler group;
    private Player player;
    private boolean isChatVisible = true;
    private boolean isPrivateMessaging = true;
    private VCScoreboard scoreboard = null;
    private boolean ready = false;

    private boolean banned = false;
    private Date tempBan = null;
    private boolean muted = false;
    private Date tempMute = null;

    private double money = 0;
    private int tokens = 0;

    private String prefix;
    private String nick;

    private HashMap<String, String> globalUserdata = new HashMap<>();
    private HashMap<String, String> userdata = new HashMap<>();

    private BukkitTask task;

    public User(final Player player) {
        this.player = player;
        group = new Group.GroupHandler(player);
        DBObject dbObject = VCUtils.getInstance().getMongoDB().query(VCUtils.mongoDBName, "Users", "UUID", player.getUniqueId().toString());
        if (dbObject != null) {
            String groupList = dbObject.get("Group") == null ? "1" : dbObject.get("Group").toString();
            for (int i : parseGroups(groupList))
                group.merge(Group.fromPermLevel(i));
            banned = dbObject.get("Banned") == null ? false : (Boolean) dbObject.get("Banned");
            tempBan = (Date) dbObject.get("TempBan");
            muted = dbObject.get("Muted") == null ? false : (Boolean) dbObject.get("Muted");
            tempMute = (Date) dbObject.get("TempMute");
            prefix = dbObject.get("Prefix") == null ? null : dbObject.get("Prefix").toString();
            nick = dbObject.get("Nick") == null ? null : dbObject.get("Nick").toString();
            Object o = dbObject.get(VCUtils.serverName + "-Money");
            money = (o == null ? 0 : (o instanceof Double ? (Double) o : (Integer) o));
            userdata = dbObject.get(VCUtils.serverName + "-UserData") == null ? new HashMap<>() : parseData(dbObject.get(VCUtils.serverName + "-UserData").toString());
            tokens = dbObject.get("Tokens") == null ? 0 : (Integer) dbObject.get("Tokens");
            globalUserdata = dbObject.get("Global-UserData") == null ? new HashMap<>() : parseData(dbObject.get("Global-UserData").toString());
        } else {
            group.merge(Group.COMMON);
        }

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
                Bukkit.getScheduler().runTask(VCUtils.getInstance(), () -> player.kickPlayer("You are banned! You can join on " + sdf.format(tempBan)));
                return;
            } else {
                Bukkit.getScheduler().runTask(VCUtils.getInstance(), () -> player.kickPlayer("You are banned!"));
                return;
            }
        }
        async_player_map.put(player, User.this);
        async_uuid_map.put(player.getUniqueId().toString(), User.this);
        this.ready = true;
        Bukkit.getPluginManager().callEvent(event);
        this.task = new UserSaveTask(player.getUniqueId().toString()).runTaskTimer(VCUtils.getInstance(), 5 * 1200, 5 * 1200);
    }

    public void addUserdata(String key, String value) {
        if (userdata.containsKey(key))
            userdata.remove(key);

        userdata.put(key, value);
    }

    public boolean hasUserdata(String key) {
        return userdata.containsKey(key);
    }

    public void removeUserdata(String key) {
        userdata.remove(key);
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

    public boolean isReady() {
        return ready;
    }

    public HashMap<String, String> getAllUserdata() {
        return userdata;
    }

    public HashMap<String, String> getAllGlobalUserdata() {
        return globalUserdata;
    }

    private boolean removed = false;

    public static void remove(final Player player) {
        final User user = async_player_map.get(player);

        if (user == null)
            return;

        if (user.isRemoved())
            return;

        user.setRemoved(true);
        Bukkit.getScheduler().runTaskAsynchronously(VCUtils.getInstance(), () -> {
            update(user);
            async_player_map.remove(player);
            async_uuid_map.remove(user.getPlayer().getUniqueId().toString());
        });

        if (user.getTask() != null)
            user.getTask().cancel();
    }

    public static void disable() {
        for (final User user : async_player_map.values()) {
            try {
                if (!user.isReady())
                    continue;

                update(user);

                if (user.getTask() != null)
                    user.getTask().cancel();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        async_player_map.clear();
    }

    public static void update(User user) {
        DBObject dbObject = VCUtils.getInstance().getMongoDB().query(VCUtils.mongoDBName, "Users", "UUID", user.getPlayer().getUniqueId().toString()) == null ? new BasicDBObject() : VCUtils.getInstance().getMongoDB().query(VCUtils.mongoDBName, "Users", "UUID", user.getPlayer().getUniqueId().toString());
        dbObject.put("UUID", user.getPlayer().getUniqueId().toString());
        dbObject.put("Group", groupsToString(user.getGroup()));
        dbObject.put("Banned", user.isBanned());
        dbObject.put("TempBan", user.getTempBan());
        dbObject.put("Muted", user.isMuted());
        dbObject.put("TempMute", user.getTempMute());
        dbObject.put("Prefix", user.getPrefix());
        dbObject.put("Nick", user.getNick());
        dbObject.put(VCUtils.serverName + "-Money", user.getMoney());
        dbObject.put(VCUtils.serverName + "-UserData", dataToString(user.getAllUserdata()));
        dbObject.put("Tokens", user.getTokens());
        dbObject.put("Global-UserData", dataToString(user.getAllGlobalUserdata()));
        DBObject dbObject1 = VCUtils.getInstance().getMongoDB().query(VCUtils.mongoDBName, "Users", "UUID", user.getPlayer().getUniqueId().toString());
        if (dbObject1 == null)
            VCUtils.getInstance().getMongoDB().insert(VCUtils.mongoDBName, "Users", dbObject);
        else
            VCUtils.getInstance().getMongoDB().update(VCUtils.mongoDBName, "Users", dbObject1, dbObject);
    }

    public void addGroup(Group group) {
        this.group.merge(group);
    }

    public void removeGroup(Group group) {
        this.group.merge(group);
    }

    public void setGroup(Group group) {
        this.group = new Group.GroupHandler(player);
        this.group.merge(group);
    }

    public void setGroup(Group.GroupHandler group) {
        this.group = group;
    }

    public Group.GroupHandler getGroup() {
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

    public String getPrefix() {
        return prefix;
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

    public BukkitTask getTask() {
        return task;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public static String dataToString(HashMap<String, String> userdata) {
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        for (Map.Entry entry : userdata.entrySet()) {
            sb.append(entry.getKey()).append("▲").append(entry.getValue());
            if (userdata.size() - 1 != counter)
                sb.append("▼");
            counter++;
        }
        return sb.toString();
    }

    public static HashMap<String, String> parseData(String data) {
        HashMap<String, String> userdata = new HashMap<>();
        if (!(data.contains("▲")))
            return userdata;
        if (data.contains("▼")) {
            String[] parts = data.split("▼");
            for (String s : parts) {
                String[] entry = s.split("▲");
                userdata.put(entry[0], entry[1]);
            }
        } else {
            String[] parts = data.split("▲");
            userdata.put(parts[0], parts[1]);
        }
        return userdata;
    }

    public static List<Integer> parseGroups(String s) {
        List<Integer> groups = new ArrayList<>();
        String[] parts = s.split(",");
        for (String part : parts) {
            try {
                groups.add(Integer.parseInt(part));
            } catch (NumberFormatException ignored) {
            }
        }
        return groups;
    }

    public static String groupsToString(Group.GroupHandler groups) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < groups.getAllGroups().size(); i++) {
            if (i + 1 == groups.getAllGroups().size())
                sb.append(groups.getAllGroups().get(i).getPermLevel());
            else
                sb.append(groups.getAllGroups().get(i).getPermLevel()).append(",");
        }
        return sb.toString();
    }

    public static DBObject getDBObject(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        return VCUtils.getInstance().getMongoDB().query(VCUtils.mongoDBName, "Users", "UUID", uuid.toString());
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }
}
