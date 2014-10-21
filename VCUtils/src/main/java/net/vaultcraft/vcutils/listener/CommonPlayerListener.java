package net.vaultcraft.vcutils.listener;

import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
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
import java.util.Set;

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

        for (Player p : VCUtils.getInstance().getServer().getOnlinePlayers()) {
            if (p != event.getPlayer() && p.getName().equalsIgnoreCase(event.getPlayer().getName())) {
                event.getPlayer().kickPlayer("Another player with your name is already on this server!");
            }
        }

        Player member = event.getPlayer();

        User thisUser = new User(member);
        int retries = 0;
        /*while (!thisUser.isReady()) {
            try {
                Thread.sleep(50);
                retries++;
                if(retries > 100) {
                    event.getPlayer().kickPlayer("We were unable to load your userdata within a short time. Try reconnecting. VCERR 001.");
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        User.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if (event.getReason() == "Logged in from another location.") {
            event.setCancelled(true);
        }

        event.setLeaveMessage(null);

        User.remove(event.getPlayer());
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        User chatter = User.fromPlayer(event.getPlayer());
        String message = event.getMessage();

        //original format <%1$s> %2$s
        String format = chatter.getGroup().getHighest().getTag();
        format = format.replace("%user%", "%1$s").replace("%message%", "%2$s");
        event.setFormat(ChatColor.translateAlternateColorCodes('&', format));

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
