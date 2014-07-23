package net.vaultcraft.vcutils.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class User {

    private static volatile ConcurrentHashMap<Player, User> async_player_map = new ConcurrentHashMap<>();

    public static User fromPlayer(Player player) {
        return async_player_map.get(player);
    }

    private Player conversation;
    private boolean editMode;
    private Group group = Group.COMMON;
    private Player player;

    private boolean banned = false;
    private Date tempBan = null;
    private boolean muted = false;
    private Date tempMute = null;

    private int money = 0;
    private int tokens = 0;

    public User(final Player player) {
        this.player = player;
        Bukkit.getScheduler().runTaskAsynchronously(VCUtils.getInstance(), new Runnable() {
            @Override
            public void run() {
                DBObject dbObject = VCUtils.getInstance().getMongoDB().query("VaultCraft", "Users", "UUID", player.getUniqueId().toString());
                if(dbObject != null) {
                    group = Group.fromPermLevel((Integer) dbObject.get("Group"));
                    banned = (Boolean) dbObject.get("Banned");
                    tempBan = (Date) dbObject.get("TempBan");
                    muted = (Boolean) dbObject.get("Muted");
                    tempMute = (Date) dbObject.get("TempMute");
                    money = (Integer) dbObject.get(VCUtils.serverName + "-Money");
                    tokens = (Integer) dbObject.get("Tokens");
                }
            }
        });
        async_player_map.put(player, this);
    }

    public static void remove(final Player player) {
        final User user = async_player_map.get(player);
        Logger.debug(VCUtils.getInstance(), user.getGroup().getPermLevel() + "");
        Bukkit.getScheduler().runTaskAsynchronously(VCUtils.getInstance(), new Runnable() {
            @Override
            public void run() {
                BasicDBObject dbObject = new BasicDBObject();
                dbObject.put("UUID", player.getUniqueId().toString());
                dbObject.put("Group", user.getGroup().getPermLevel());
                dbObject.put("Banned", user.isBanned());
                dbObject.put("TempBan", user.getTempBan());
                dbObject.put("Muted", user.isMuted());
                dbObject.put("TempMute", user.getTempMute());
                dbObject.put(VCUtils.serverName + "-Money", user.getMoney());
                dbObject.put("Tokens", user.getTokens());
                DBObject dbObject1 = VCUtils.getInstance().getMongoDB().query("VaultCraft", "Users", "UUID", player.getUniqueId().toString());
                if(dbObject1 == null)
                    VCUtils.getInstance().getMongoDB().insert("VaultCraft", "Users", dbObject);
                else
                    VCUtils.getInstance().getMongoDB().update("VaultCraft", "Users", dbObject1, dbObject);
            }
        });
        async_player_map.remove(player);
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

    public int getMoney() {
        return money;
    }

    public int getTokens() {
        return tokens;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public void addMoney(int addTo) {
        money += addTo;
    }

    public void addTokens(int addTo) {
        tokens += addTo;
    }
}
