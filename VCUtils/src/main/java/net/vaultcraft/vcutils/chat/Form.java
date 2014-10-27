package net.vaultcraft.vcutils.chat;

import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class Form {

    public static void at(Player player, Prefix prefix, String message) {
        String sent = ChatColor.translateAlternateColorCodes('&', prefix.getPrefix()+message+prefix.getSuffix());
        player.sendMessage(sent);
    }

    public static void atCharacter(Player player, Prefix prefix, String message, String character) {
        String sent = ChatColor.translateAlternateColorCodes('&', prefix.getPrefix().replace("{0}", character)+message+prefix.getSuffix());
        player.sendMessage(sent);
    }

    public static void at(Player player, String message) {
        at(player, Prefix.VAULT_CRAFT, message);
    }

    public static void atHelp(Player player, ICommand command) {
        at(player, "Help for - \""+command.getName()+"\"");
        if (command.isDisplayingGroup()) {
            for (String m : command.getHelp().keySet()) {
                String value = command.getHelp().get(m);
                Group g = command.getGroupPerms().get(m);
                g = (g == null ? Group.COMMON : g);
                at(player, Prefix.NOTHING, "&c/"+command.getName()+" "+m+" &e- &7"+value+" &4("+g.getName()+")");
            }
            return;
        }
        for (String m : command.getHelp().keySet()) {
            String value = command.getHelp().get(m);
            at(player, Prefix.NOTHING, "&c/"+command.getName()+" "+m+" &e- &7"+value);
        }
    }

    private static DecimalFormat $form = new DecimalFormat("#,##0.00");

    public static String at(double value, boolean more) {
        if(!more)
            return $form.format(value);
        if(value / 1000000000d > 9999)
            return $form.format(value / 1000000000000d) + " tril";
        if(value / 1000000d > 9999)
            return $form.format(value / 1000000000d) + " bil";
        if(value / 1000d > 9999)
            return $form.format(value / 1000000d) + " mil";
        if( value > 9999)
            return $form.format(value / 1000d) + "k";
        return $form.format(value);
    }

    private static DecimalFormat $intForm = new DecimalFormat("#,###");

    public static String at(int value) {
        $intForm.setMaximumFractionDigits(0);
        return $intForm.format(value);
    }
}
