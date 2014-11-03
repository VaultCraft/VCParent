package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.OfflineUser;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Created by tacticalsk8er on 7/22/2014.
 */
public class VCMoney extends ICommand{
    public VCMoney(String name, Group permission, String... aliases) {
        super(name, permission, aliases);

        this.display_group = true;
        subCmds.put("pay <user> <amount>", "Pay a user the given amount"); groupPerms.put("pay <user> <amount", Group.COMMON);
        subCmds.put("add <user> <amount>", "Add the given amount to a users balance"); groupPerms.put("add <user> <amount>", Group.ADMIN);
        subCmds.put("set <user> <amount>", "Set a users balance to the given amount"); groupPerms.put("set <user> <amount>", Group.ADMIN);
        subCmds.put("<user>", "Check the given users balance"); groupPerms.put("<user>", Group.COMMON);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if(args.length == 0) {
            Form.at(player, Prefix.NOTHING, "&a&lMoney&f: $" + Form.at(User.fromPlayer(player).getMoney(), true));
            return;
        }

        if(args.length >= 1) {

            switch (args[0]) {
                case "help":
                    Form.atHelp(player, this);
                    break;
                case "pay":
                    if(args.length < 3) {
                        Form.at(player, Prefix.ERROR, "Format: /money pay <player> <amount>");
                        return;
                    }

                    Player player1 = Bukkit.getPlayer(args[1]);
                    double amount;
                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        Form.at(player, Prefix.ERROR, "Argument 3 needs to be a integer.");
                        return;
                    }

                    if(amount <= 0) {
                        Form.at(player, Prefix.ERROR, "Argument 3 needs to be a positive integer.");
                        return;
                    }

                    if(User.fromPlayer(player).getMoney() < amount) {
                        Form.at(player, Prefix.ERROR, "You don't have enough money.");
                        return;
                    }

                    if (player1 == null) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                        if(offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                            OfflineUser user = OfflineUser.getOfflineUser(offlinePlayer);
                            User.fromPlayer(player).addMoney(-amount);
                            user.addMoney(amount);
                            Form.at(player, Prefix.SUCCESS, "You sent " + offlinePlayer.getName() + " $" + Form.at(amount, true) + ".");
                            return;
                        }
                        Form.at(player, Prefix.ERROR, "No such player");
                        return;
                    }

                    User.fromPlayer(player).addMoney(-amount);
                    User.fromPlayer(player1).addMoney(amount);
                    Form.at(player, Prefix.SUCCESS, "You sent " + player1.getName() + " $" + Form.at(amount, true) + ".");
                    Form.at(player1, Prefix.VAULT_CRAFT, player.getName() + " sent you $" + Form.at(amount, true) + ".");
                    break;
                case "add":
                    if(!User.fromPlayer(player).getGroup().hasPermission(Group.ADMIN)) {
                        Form.at(player, Prefix.ERROR, "You do not have permission to use this command!");
                        return;
                    }

                    if(args.length < 3) {
                        Form.at(player, Prefix.ERROR, "Format: /money add <player> <amount>");
                        return;
                    }

                    player1 = Bukkit.getPlayer(args[1]);
                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        Form.at(player, Prefix.ERROR, "Argument 3 needs to be a integer.");
                        return;
                    }

                    if (player1 == null) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                        if(offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                            OfflineUser user = OfflineUser.getOfflineUser(offlinePlayer);
                            if(user.getMoneyOld() < -amount)
                                amount = -user.getMoneyOld();
                            user.addMoney(amount);
                            Form.at(player, Prefix.SUCCESS, offlinePlayer.getName() + " now has $" + Form.at((user.getMoneyOld() - amount), true) + ".");
                            return;
                        }
                        Form.at(player, Prefix.ERROR, "No such player");
                        return;
                    }

                    if(User.fromPlayer(player1).getMoney() < -amount) {
                        amount = -User.fromPlayer(player1).getMoney();
                    }

                    User.fromPlayer(player1).addMoney(amount);
                    Form.at(player, Prefix.SUCCESS, player1.getName() + " now has $" + Form.at(User.fromPlayer(player1).getMoney(), true) + ".");
                    break;
                case "set":
                    if(!User.fromPlayer(player).getGroup().hasPermission(Group.ADMIN)) {
                        Form.at(player, Prefix.ERROR, "You do not have permission to use this command!");
                        return;
                    }

                    if(args.length < 3) {
                        Form.at(player, Prefix.ERROR, "Format: /money set <player> <amount>");
                        return;
                    }

                    player1 = Bukkit.getPlayer(args[1]);
                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        Form.at(player, Prefix.ERROR, "Argument 3 needs to be a integer.");
                        return;
                    }

                    if(amount <= 0) {
                        Form.at(player, Prefix.ERROR, "Argument 3 needs to be a positive integer.");
                        return;
                    }

                    if (player1 == null) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                        if(offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                            OfflineUser user = OfflineUser.getOfflineUser(offlinePlayer);
                            double change = amount - user.getMoneyOld();
                            user.addMoney(change);
                            Form.at(player, Prefix.SUCCESS, offlinePlayer.getName() + " now has $" + Form.at((user.getMoneyOld() + change), true) + ".");
                            return;
                        }
                        Form.at(player, Prefix.ERROR, "No such player");
                        return;
                    }

                    User.fromPlayer(player1).setMoney(amount);
                    Form.at(player, Prefix.SUCCESS, player1.getName() + " now has $" + Form.at(User.fromPlayer(player1).getMoney(), true) + ".");
                    break;
                default:
                    player1 = Bukkit.getPlayer(args[0]);
                    if (player1 == null) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                        if(offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                            OfflineUser user = OfflineUser.getOfflineUser(offlinePlayer);
                            Form.at(player, Prefix.VAULT_CRAFT,  offlinePlayer.getName() + " has &a$" + Form.at((user.getMoneyOld() + user.getChangeInMoney()), true) + ".");
                            return;
                        }
                        Form.at(player, Prefix.ERROR, "No such player");
                        return;
                    }
                    Form.at(player, Prefix.VAULT_CRAFT,  player1.getName() + " has &a$" + Form.at(User.fromPlayer(player1).getMoney(), true) + ".");
                    break;
            }
        }
    }
}
