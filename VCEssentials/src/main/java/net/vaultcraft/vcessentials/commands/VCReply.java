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

public class VCReply extends ICommand {

    public VCReply(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        if (args.length < 1) {
            Form.at(player, Prefix.ERROR, "Format: /r <message>");
            return;
        }

        if (!(User.fromPlayer(player).isPrivateMessaging())) {
            Form.at(player, Prefix.WARNING, "You cannot private message until you enable private messaging!");
            return;
        }

        if(User.fromPlayer(player).isMuted()) {
            Form.at(player, Prefix.WARNING, "You cannot private message while you are muted!");
            return;
        }

        User user = User.fromPlayer(player);
        if (user.modifyConversation(null) == null) {
            Form.at(player, Prefix.ERROR, "You are not chatting with anyone!");
            return;
        }

        Player find = user.modifyConversation(null);
        if (find == null) {
            Form.at(player, Prefix.ERROR, "You are not chatting with anyone!");
            return;
        }

        User findUser = User.fromPlayer(find);
        if(findUser == null) {
            return;
        }

        if (!findUser.isPrivateMessaging()) {
            Form.at(player, Prefix.WARNING, "The player you were talking to is no longer receiving private messages!");
            return;
        }

        if(VCChatListener.afkPlayers.contains(find)) {
            Form.at(player, Prefix.WARNING, find.getName() + " is currently AFK and may not respond to your message!");
        }

        if(VCIgnore.isIgnored(find, player) && !User.fromPlayer(find).getGroup().hasPermission(Group.HELPER)) {
            Form.at(player, Prefix.WARNING, find.getName() + " is ignoring you and will not receive this message.");
            return;
        }

        String message = StringUtils.buildFromArray(args);

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
    }
}
