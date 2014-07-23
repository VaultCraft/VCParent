package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by tacticalsk8er on 7/22/2014.
 */
public class VCToken extends ICommand {
    public VCToken(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if(args.length == 0) {
            Form.at(player, Prefix.VAULT_CRAFT, "&6Tokens&f: " + User.fromPlayer(player).getTokens());
            return;
        }

        if(args.length == 1) {

            switch (args[0]) {
                case "pay":
                    if(args.length < 3) {
                        Form.at(player, Prefix.ERROR, "Format: /tokens pay <player> <amount>");
                        return;
                    }

                    Player player1 = Bukkit.getPlayer(args[1]);
                    int amount;
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        Form.at(player, Prefix.ERROR, "Argument 3 needs to be a integer.");
                        return;
                    }

                    if(amount <= 0) {
                        Form.at(player, Prefix.ERROR, "Argument 3 needs to be a positive integer.");
                        return;
                    }

                    if(player1 == null) {
                        Form.at(player, Prefix.ERROR, "Player: " + args[1] + " is not online.");
                        return;
                    }

                    if(User.fromPlayer(player).getTokens() < amount) {
                        Form.at(player, Prefix.ERROR, "You don't have enough tokens.");
                        return;
                    }

                    User.fromPlayer(player).addTokens(-amount);
                    User.fromPlayer(player1).addTokens(amount);
                    Form.at(player, Prefix.SUCCESS, "You sent " + player1.getName() + " " + amount + " tokens.");
                    Form.at(player1, Prefix.VAULT_CRAFT, player.getName() + " sent you " + amount + " tokens.");
                    break;
                case "add":
                    if(!User.fromPlayer(player).getGroup().hasPermission(Group.OWNER)) {
                        Form.at(player, Prefix.ERROR, "You do not have permission to use this command!");
                        return;
                    }

                    if(args.length < 3) {
                        Form.at(player, Prefix.ERROR, "Format: /tokens add <player> <amount>");
                        return;
                    }

                    player1 = Bukkit.getPlayer(args[1]);
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        Form.at(player, Prefix.ERROR, "Argument 3 needs to be a integer.");
                        return;
                    }

                    if(player1 == null) {
                        Form.at(player, Prefix.ERROR, "Player: " + args[1] + " is not online.");
                        return;
                    }

                    if(User.fromPlayer(player1).getTokens() < -amount) {
                        amount = User.fromPlayer(player1).getTokens();
                    }

                    User.fromPlayer(player1).addTokens(amount);
                    Form.at(player, Prefix.SUCCESS, player1.getName() + " now has " + User.fromPlayer(player1).getTokens() + " tokens.");
                    break;
                case "set":
                    if(!User.fromPlayer(player).getGroup().hasPermission(Group.OWNER)) {
                        Form.at(player, Prefix.ERROR, "You do not have permission to use this command!");
                        return;
                    }

                    if(args.length < 3) {
                        Form.at(player, Prefix.ERROR, "Format: /tokens set <player> <amount>");
                        return;
                    }

                    player1 = Bukkit.getPlayer(args[1]);
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        Form.at(player, Prefix.ERROR, "Argument 3 needs to be a integer.");
                        return;
                    }

                    if(player1 == null) {
                        Form.at(player, Prefix.ERROR, "Player: " + args[1] + " is not online.");
                        return;
                    }

                    if(amount <= 0) {
                        Form.at(player, Prefix.ERROR, "Argument 3 needs to be a positive integer.");
                        return;
                    }

                    User.fromPlayer(player1).setTokens(amount);
                    Form.at(player, Prefix.SUCCESS, player1.getName() + " now has " + User.fromPlayer(player1).getTokens() + "tokens.");
                    break;
                default:
                    player1 = Bukkit.getPlayer(args[1]);
                    if(player1 == null) {
                        Form.at(player, Prefix.ERROR, "Player: " + args[1] + " is not online.");
                        return;
                    }
                    Form.at(player, Prefix.VAULT_CRAFT,  player1.getName() + " has &6" + User.fromPlayer(player1).getTokens() + " tokens.");
                    break;
            }
        }
    }
}
