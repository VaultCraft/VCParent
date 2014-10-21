package net.vaultcraft.vcutils.tab;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.spigotmc.ProtocolInjector;

/**
 * @author Nicholas Peterson
 */
public class TabList {

    private String header = "";
    private String footer = "";

    public TabList(String header, String footer) {
        this.header = header;
        this.footer = footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void send(Player player) {
        if(((CraftPlayer) player).getHandle().playerConnection.networkManager.getVersion() < 47)
            return;
        IChatBaseComponent chatHeader = ChatSerializer.a("{\"Text:\" \"" + header + "\"");
        IChatBaseComponent chatFooter = ChatSerializer.a("{\"Text:\" \"" + footer + "\"");
        ProtocolInjector.PacketTabHeader packet = new ProtocolInjector.PacketTabHeader(chatHeader, chatFooter);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
