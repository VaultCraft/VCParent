package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.string.StringUtils;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class VCMessage extends ICommand {

    public VCMessage(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    /*
     * Message format
     *   From player {from}
     *    &5&l[&7&ome &5&l-> &7&o{to}&5&l] &7{message}
     *   To player {to}
     *    &5&l[&7&o{from} &5&l-> &7&ome&5&l] &7{message}
     */
    public void processCommand(Player player, String[] args) {
        if (args.length < 2) {
            Form.at(player, Prefix.ERROR, "Format: /msg <user> <message>");
            return;
        }

        if (!(User.fromPlayer(player).isPrivateMessaging())) {
            Form.at(player, Prefix.WARNING, "You cannot private message until you enable private messaging!");
            return;
        }

        Player find = Bukkit.getPlayer(args[0]);
        if (find == null) {
            Form.at(player, Prefix.ERROR, "No such player! Format: /msg <user> <message>");
            return;
        }

        if (find.equals(player)) {
            Form.at(player, Prefix.WARNING, "Why are you trying to message yourself?");
            return;
        }

        if (!User.fromPlayer(find).isPrivateMessaging()) {
            Form.at(player, Prefix.WARNING, "You cannot messages this player as they are not private messaging!");
            return;
        }

        String message = StringUtils.buildFromArray(args, 1);

        Form.at(player, Prefix.NOTHING, "&5&l[&7&ome &5&l-> &7&o{to}&5&l] &7{message}"
                .replace("{message}", message)
                .replace("{to}", find.getName()));
        Form.at(find, Prefix.NOTHING, "&5&l[&7&o{from} &5&l-> &7&ome&5&l] &7{message}"
                .replace("{message}", message)
                .replace("{from}", player.getName()));
        find.playSound(find.getLocation(), Sound.NOTE_PIANO, 1, 1);

        User.fromPlayer(player).modifyConversation(find);
    }
}
