package net.vaultcraft.vcutils.listener;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.network.PacketReceivedEvent;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.UpdatedUserData;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.spigotmc.ProtocolInjector;

import java.io.IOException;
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

        VCUtils.getInstance().getServer().getOnlinePlayers().stream().filter(p -> p != event.getPlayer() && p.getName().equalsIgnoreCase(event.getPlayer().getName())).forEach(p -> {
            event.getPlayer().kickPlayer("Another player with your name is already on this server!");
        });

        Player member = event.getPlayer();

        if (validate1_8(member)) {
            PlayerConnection con = ((CraftPlayer)member).getHandle().playerConnection;
            IChatBaseComponent head = ChatSerializer.a("{'color': 'dark_purple', 'text': 'VaultCraft Network'}");
            IChatBaseComponent foot = ChatSerializer.a("{'color': 'gray', 'text': 'mc.vaultcraft.net'}");
            con.sendPacket(new ProtocolInjector.PacketTabHeader(head, foot));
        }

        new User(member);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        User theUser = User.fromPlayer(event.getPlayer());
        if(!theUser.isReady()) {
            event.setCancelled(true);
            Form.at(event.getPlayer(), Prefix.WARNING, "Your userdata has not yet been loaded. If this lasts more than a second please rejoin.");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        User.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if (event.getReason().equals("Logged in from another location.")) {
            event.setCancelled(true);
        }

        event.setLeaveMessage(null);

        User.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        User chatter = User.fromPlayer(event.getPlayer());
        String message = event.getMessage();

        //original format <%1$s> %2$s
        String format = chatter.getGroup().getHighest().getTag();
        if (chatter.getPrefix() != null)
            format = chatter.getPrefix();

        format = format.replace("%user%", "%1$s").replace("%message%", "%2$s");

        if (chatter.getNick() != null)
            format = format.replace("%1$s", chatter.getNick());

        event.setFormat(ChatColor.translateAlternateColorCodes('&', format));

        if (chatter.getGroup().hasPermission(Group.SKELETON) || chatter.getGroup().hasPermission(Group.HELPER))
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

    private static boolean validate1_8(Player player) {
        return ((CraftPlayer)player).getHandle().playerConnection.networkManager.getVersion() >= 47;
    }

    @EventHandler
    public void onPacket(PacketReceivedEvent event) {
        if(!event.getChannel().equals("update-user"))
            return;
        try {
            UpdatedUserData userData = (UpdatedUserData) event.getStream().readObject();
            if(User.fromUUID(userData.playerUUID) == null)
                return;
            userData.updateUser(User.fromUUID(userData.playerUUID));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
