package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class VCPromote extends ICommand {

    public VCPromote(String name, Group group, String... args) {
        super(name, group, args);
    }

    /*
     * Format: /promote <user> <rank>
     */
    public void processCommand(Player player, String[] args) {
        if (args.length < 2) {
            Form.at(player, Prefix.ERROR, "Format: /setgroup <user> <rank>");
            return;
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (args.length < 3) {
                Form.at(player, Prefix.ERROR, "Format: /setgroup add <user> <rank>");
                return;
            }

            Player wrapped = Bukkit.getPlayer(args[1]);
            if (wrapped == null) {
                Form.at(player, Prefix.ERROR, "No such player! Format: /setgroup add <user> <rank>");
                return;
            }

            Group add = Group.fromString(args[2]);
            if (add == null) {
                Form.at(player, Prefix.ERROR, "No such group! Format: /setgroup add <user> <rank>");
                return;
            }

            User.fromPlayer(wrapped).getGroup().merge(add);
            Form.at(player, Prefix.SUCCESS, "Added group: &e" + add.getName() + Prefix.SUCCESS.getChatColor()+" to player &e " + wrapped.getName() + Prefix.SUCCESS.getChatColor()+ "!");
            return;
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length < 3) {
                Form.at(player, Prefix.ERROR, "Format: /setgroup remove <user> <rank>");
                return;
            }

            Player wrapped = Bukkit.getPlayer(args[1]);
            if (wrapped == null) {
                Form.at(player, Prefix.ERROR, "No such player! Format: /setgroup remove <user> <rank>");
                return;
            }

            Group add = Group.fromString(args[2]);
            if (add == null) {
                Form.at(player, Prefix.ERROR, "No such group! Format: /setgroup remove <user> <rank>");
                return;
            }

            User.fromPlayer(wrapped).getGroup().remove(add);
            Form.at(player, Prefix.SUCCESS, "Removed group: &e" + add.getName() + Prefix.SUCCESS.getChatColor()+" from player &e " + wrapped.getName() + Prefix.SUCCESS.getChatColor()+ "!");
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
        Form.at(player, Prefix.SUCCESS, "&e" + wrapped.getName() + Prefix.SUCCESS.getChatColor() + " promoted to &e" + select.getName() + Prefix.SUCCESS.getChatColor() + "!");
    }
}
