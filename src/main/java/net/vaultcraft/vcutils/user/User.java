package net.vaultcraft.vcutils.user;

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

    public User(Player player) {
        this.player = player;
        async_player_map.put(player, this);
    }

    public static void remove(Player player) {
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
}
