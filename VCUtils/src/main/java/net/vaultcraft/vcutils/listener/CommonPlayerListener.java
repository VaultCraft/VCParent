package net.vaultcraft.vcutils.listener;

import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.database.sql.MySQL;
import net.vaultcraft.vcutils.database.sql.Statements;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import net.vaultcraft.vcutils.user.localdata.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class CommonPlayerListener implements Listener {

    private static CommonPlayerListener instance;

    public static CommonPlayerListener getInstance() {
        return instance;
    }

    public CommonPlayerListener() {
        instance = this;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player member = event.getPlayer();
        new User(member);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        final HashMap<String, String> data = User.fromPlayer(event.getPlayer()).getAllUserdata();
        final UUID pUUID = event.getPlayer().getUniqueId();

        Bukkit.getScheduler().scheduleAsyncDelayedTask(VCUtils.getInstance(), new Runnable() { public void run() { DataManager.saveUserdata(data, pUUID); } });
        User.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        event.setLeaveMessage(null);
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        User chatter = User.fromPlayer(event.getPlayer());
        String message = event.getMessage();

        //original format <%1$s> %2$s
        String format = chatter.getGroup().getTag();
        format = format.replace("%user%", "%1$s").replace("%message%", "%2$s");
        event.setFormat(ChatColor.translateAlternateColorCodes('&', format));

        VCUtils.getInstance().getMySQL().updateThread.add(Statements.INSERT.getSql("Chat",
                "'" + chatter.getPlayer().getUniqueId().toString() + "', '" +
                        chatter.getPlayer().getName() + "', '" +
                        event.getMessage() + "', '" +
                        MySQL.getDate() + "'"
        ));

        if (chatter.getGroup().hasPermission(Group.HELPER))
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));

        //hide for invis players
        if (!chatter.isChatVisible()) {
            Form.at(chatter.getPlayer(), Prefix.WARNING, "You cannot chat when your chat is disabled!");
            event.setCancelled(true);
        }

        Set<Player> received = event.getRecipients();
        for (Player player : User.async_player_map.keySet()) {
            if (User.fromPlayer(player).isChatVisible() || User.fromPlayer(player).getGroup().hasPermission(Group.HELPER))
                continue;

            received.remove(player);
        }

        try {
            Field set = event.getClass().getDeclaredField("recipients");

            Field modifier = Field.class.getDeclaredField("modifiers");
            modifier.setAccessible(true);
            modifier.setInt(set, set.getModifiers() & ~Modifier.FINAL);

            set.setAccessible(true);
            set.set(event, received);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
