package net.vaultcraft.vcutils.title;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.spigotmc.ProtocolInjector;

/**
 * @author Nicholas Peterson
 */
public class Title {

    private String title = "";
    private String subtitle = "";

    private int fadeIn = 10;
    private int stay = 100;
    private int fadeOut = 10;

    public Title (String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public Title fadeIn(int ticks) {
        fadeIn = ticks;
        return this;
    }

    public Title stay(int ticks) {
        stay = ticks;
        return this;
    }

    public Title fadeOut(int ticks) {
        fadeOut = ticks;
        return this;
    }

    public void sendTitle(Player player) {
        if(((CraftPlayer) player).getHandle().playerConnection.networkManager.getVersion() < 47)
            return;
        IChatBaseComponent chatTitle = ChatSerializer.a("{'color': 'dark_purple', 'text': '" + title + "'");
        IChatBaseComponent chatSubtitle = ChatSerializer.a("{'color': 'gray', 'text': '" + subtitle + "'");
        ProtocolInjector.PacketTitle titlePacket = new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.TITLE, chatTitle);
        ProtocolInjector.PacketTitle subtitlePacket = new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.SUBTITLE, chatSubtitle);
        ProtocolInjector.PacketTitle timesPacket = new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.TIMES, fadeIn, stay, fadeOut);
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(timesPacket);
        if(!title.isEmpty())
            connection.sendPacket(titlePacket);
        if(!subtitle.isEmpty())
            connection.sendPacket(subtitlePacket);
    }
}
