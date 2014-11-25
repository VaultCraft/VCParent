package net.vaultcraft.vcutils.chat;

import net.minecraft.server.v1_7_R4.ChatComponentText;
import net.minecraft.server.v1_7_R4.ChatMessage;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;
import net.vaultcraft.vcutils.chat.fancy.JSONChatMessage;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * @author Nicholas Peterson
 */
public class ItemMessage {

    public static void sendMessage(Player player, String text) {
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(text), 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static void sendMesssage(Player player, JSONChatMessage message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatMessage(message.toString()), 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
