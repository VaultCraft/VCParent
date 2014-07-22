package net.vaultcraft.vcutils.listener;

import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.database.sql.MySQL;
import net.vaultcraft.vcutils.database.sql.Statements;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player member = event.getPlayer();
        User user = new User(member);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

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

        if (chatter.getGroup().hasPermission(Group.HELPER)) {
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        }

        VCUtils.getInstance().mySQL.updateThread.add(Statements.INSERT.getSql("Chat",
                "'" + chatter.getPlayer().getUniqueId().toString() + "', '" +
                        chatter.getPlayer().getName() + "', '" +
                        event.getMessage() + "', " +
                        MySQL.getDate()));
    }
}
