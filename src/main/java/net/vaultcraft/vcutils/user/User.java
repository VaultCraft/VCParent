package net.vaultcraft.vcutils.user;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class User {

    private static volatile ConcurrentHashMap<Player, User> async_player_map = new ConcurrentHashMap<>();

    public static User fromPlayer(Player player) {
        return async_player_map.get(player);
    }

    private Group group = Group.COMMON;
    private Player player;

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
}
