package net.vaultcraft.vcutils.chat;

import net.vaultcraft.vcutils.command.ICommand;
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

    public static void atHelp(Player player, ICommand command) {
        at(player, "Help for - \""+command.getName()+"\"");
        for (String m : command.getHelp().keySet()) {
            String value = command.getHelp().get(m);
            at(player, Prefix.NOTHING, "&c/"+command.getName()+" "+m+" &e- &7"+value);
        }
    }

    public static void OIJFEO() {}
}
