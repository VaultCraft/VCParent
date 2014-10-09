package net.vaultcraft.vcutils.user;

import common.network.PacketInUserGet;
import common.network.PacketInUserSend;
import common.network.UserInfo;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.network.MessageClient;
import net.vaultcraft.vcutils.scoreboard.VCScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

    private HashMap<String, String> globalUserdata = new HashMap<>();
    private HashMap<String, String> userdata = new HashMap<>();

    public User(final Player player) {
        this.player = player;
        async_player_map.put(player, User.this);
        async_uuid_map.put(player.getUniqueId().toString(), User.this);
        MessageClient.sendPacket(new PacketInUserGet(player.getUniqueId().toString(), VCUtils.serverName));
        new UserSaveTask(player.getUniqueId().toString());
    }

    public void setUserInfo(UserInfo info) {
        for(int i = 0; i < info.getGroups().size(); i++) {
            int permLevel = (Integer) info.getGroups().get(i);
            if(i == 0) {
                this.group.merge(Group.fromPermLevel(permLevel));
                continue;
            }
            this.group.merge(Group.fromPermLevel(permLevel));
        }
        this.banned = info.isBanned();
        this.tempBan = info.getTempBan();
        this.muted = info.isMuted();
        this.tempMute = info.getTempMute();
        this.money = info.getMoney();
        this.tokens = info.getTokens();
        this.globalUserdata = info.getGlobalUserdata();
        this.userdata = info.getUserdata();
        //Check if banned
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
        this.ready = true;
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

    public boolean isReady() {
        return ready;
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
                if (user.isReady())
                    MessageClient.sendPacket(new PacketInUserSend(user.getPlayer().getUniqueId().toString(), VCUtils.serverName, new UserInfo("", user.getPlayer().getUniqueId().toString())));
                async_player_map.remove(player);
                async_uuid_map.remove(player.getUniqueId().toString());
            }
        });
    }

    public static void disable() {
        for (final User user : async_player_map.values()) {
            if (!user.isReady())
                continue;
            MessageClient.sendPacket(new PacketInUserSend(user.getPlayer().getUniqueId().toString(), VCUtils.serverName, new UserInfo("", user.getPlayer().getUniqueId().toString())));
        }
        async_player_map.clear();
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
}
