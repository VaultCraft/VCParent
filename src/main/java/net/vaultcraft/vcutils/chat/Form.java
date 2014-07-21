package net.vaultcraft.vcutils.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class Form {

    public static void at(Player player, Prefix prefix, String message) {
        String sent = ChatColor.translateAlternateColorCodes('&', prefix.getPrefix()+message+prefix.getSuffix());
        //log to sql or such
        player.sendMessage(sent);
    }

    public static void at(Player player, String message) {
        at(player, Prefix.VAULT_CRAFT, message);
    }

    public static String at(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String at(Prefix prefix, String message) {
        return ChatColor.translateAlternateColorCodes('&', prefix.getPrefix()+message+prefix.getSuffix());
    }
}
