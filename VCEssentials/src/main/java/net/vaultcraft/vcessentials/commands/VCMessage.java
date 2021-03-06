package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcessentials.listeners.VCChatListener;
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

        if (User.fromPlayer(player).isMuted()) {
            Form.at(player, Prefix.WARNING, "You cannot private message while you are muted!");
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


        if (VCChatListener.afkPlayers.contains(find)) {
            Form.at(player, Prefix.WARNING, find.getName() + " is currently AFK and may not respond to your message!");
        }

        if(VCIgnore.isIgnored(find, player) && !User.fromPlayer(find).getGroup().hasPermission(Group.HELPER)) {
            Form.at(player, Prefix.WARNING, find.getName() + " is ignoring you and will not receive this message.");
            return;
        }

        String message = StringUtils.buildFromArray(args, 1);

        Form.at(player, Prefix.NOTHING, "&5&l[&7&ome &5&l-> &7&o{to}&5&l] &7{message}"
                .replace("{message}", message)
                .replace("{to}", find.getName()));
        for (String s : VCSocialSpy.getSpyList()) {
            Player spy = Bukkit.getPlayer(s);
            if (spy != null)
                if (!spy.equals(player) && !spy.equals(find))
                    Form.at(spy, Prefix.NOTHING, "&5&l[&7&o{from} &5&l-> &7&o{to}&5&l] &7{message}"
                            .replace("{message}", message)
                            .replace("{to}", find.getName())
                            .replace("{from}", player.getName()));
        }
        Form.at(find, Prefix.NOTHING, "&5&l[&7&o{from} &5&l-> &7&ome&5&l] &7{message}"
                .replace("{message}", message)
                .replace("{from}", player.getName()));
        find.playSound(find.getLocation(), Sound.NOTE_PIANO, 1, 1);

        User.fromPlayer(player).modifyConversation(find);
    }
}
