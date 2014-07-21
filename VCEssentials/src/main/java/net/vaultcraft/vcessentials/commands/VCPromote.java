package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class VCPromote extends ICommand {

    public VCPromote() {
        super("promote", Group.ADMIN, "demote", "setgroup");
    }

    /*
     * Format: /promote <user> <rank>
     */
    public void processCommand(Player player, String[] args) {
        if (args.length < 2) {
            Form.at(player, Prefix.ERROR, "Format: /setgroup <user> <rank>");
            return;
        }

        Player wrapped = Bukkit.getPlayer(args[0]);
        if (wrapped == null) {
            Form.at(player, Prefix.ERROR, "No such player! Format: /setgroup <user> <rank>");
            return;
        }

        Group select = Group.fromString(args[1]);
        if (select == null) {
            Form.at(player, Prefix.ERROR, "No such group! Format: /setgroup <user> <rank>");
            return;
        }

        User.fromPlayer(wrapped).setGroup(select);
        Form.at(player, Prefix.SUCCESS, "&e"+wrapped.getName()+Prefix.SUCCESS.getChatColor()+" promoted to &e"+select.getName()+Prefix.SUCCESS.getChatColor()+"!");
    }
}
