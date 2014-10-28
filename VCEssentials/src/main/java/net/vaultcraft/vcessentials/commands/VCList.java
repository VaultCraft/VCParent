package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Connor Hollasch
 * @since 10/27/2014
 */
public class VCList extends ICommand {

    public VCList(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        Form.at(player, Prefix.VAULT_CRAFT, "Online players ( " + ChatColor.RED + Bukkit.getOnlinePlayers().size() + Prefix.VAULT_CRAFT.getChatColor() + ")");

        String online = "";

        for (Player p : Bukkit.getOnlinePlayers()) {
            User u = User.fromPlayer(p);
            ChatColor c = u.getGroup().getHighest().getChatColor();

            online+=(c+p.getName() + ChatColor.GRAY + ", ");
        }

        if (online.endsWith(", "))
            online = online.substring(0, online.length()-2);

        Form.at(player, Prefix.VAULT_CRAFT, online);
    }
}
