package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VCIgnore extends ICommand {
    public VCIgnore(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if(args.length < 1) {
            Form.at(player, Prefix.VAULT_CRAFT, "Usage: /ignore <add/remove/list> <player name>");
            return;
        }

        switch (args[0].toLowerCase()) {
            default:
                Form.at(player, Prefix.VAULT_CRAFT, "Usage: /ignore <add/remove/list> <player name>");
                return;
            case "add":
                if(args.length < 2) {
                    Form.at(player, Prefix.VAULT_CRAFT, "Usage: /ignore <add/remove/list> <player name>");
                    return;
                }
                Player thePlayer = Bukkit.getPlayer(args[1]);
                if(thePlayer == null || !thePlayer.isOnline()) {
                    Form.at(player, Prefix.ERROR, "That player either does not exist or is not online.");
                    return;
                }
                if(thePlayer == player) {
                    Form.at(player, Prefix.ERROR, "You can't ignore yourself!");
                    return;
                }
                User theUser = User.fromPlayer(player);

                if (theUser.getUserdata("IgnoredUsers") == null)
                    theUser.addUserdata("IgnoredUsers", thePlayer.getName()+",");
                else
                    theUser.addUserdata("IgnoredUsers", theUser.getUserdata("IgnoredUsers") + thePlayer.getName() + ",");

                Form.at(player, Prefix.SUCCESS, "Successfully ignored " + thePlayer.getName());
                return;
            case "list":
                Form.at(player, Prefix.VAULT_CRAFT, "Ignored users:");
                if (User.fromPlayer(player).getUserdata("IgnoredUsers") == null)
                    Form.at(player, Prefix.VAULT_CRAFT, "None!");
                else {
                    for (String pName : User.fromPlayer(player).getUserdata("IgnoredUsers").split(",")) {
                        Form.at(player, Prefix.VAULT_CRAFT, pName);
                    }
                }
                return;
            case "remove":
                if(args.length < 2) {
                    Form.at(player, Prefix.VAULT_CRAFT, "Usage: /ignore <add/remove/list> <player name>");
                    return;
                }
                if(!isIgnored(player, args[1])) {
                    Form.at(player, Prefix.ERROR, "You aren't ignoring " + args[1] + "!");
                    return;
                }
                User.fromPlayer(player).addUserdata("IgnoredUsers", User.fromPlayer(player).getUserdata("IgnoredUsers").replaceAll(args[1] + ",", ""));
                Form.at(player, Prefix.SUCCESS, "Successfully unignored " + args[1]);
        }
    }

    public static boolean isIgnored(Player receiver, String senderName) {
        User recv = User.fromPlayer(receiver);
        if(recv == null) {
            return false;
        }

        String listOfNames = recv.getUserdata("IgnoredUsers");
        if(listOfNames == null || listOfNames.equals("")) {
            return false;
        }

        for(String s : recv.getUserdata("IgnoredUsers").split(",")) {
            if(s.equalsIgnoreCase(senderName)) {
                return true;
            }
        }
        return false;


    }

    public static boolean isIgnored(Player receiver, Player player) {
        return isIgnored(receiver, player.getName());
    }
}
