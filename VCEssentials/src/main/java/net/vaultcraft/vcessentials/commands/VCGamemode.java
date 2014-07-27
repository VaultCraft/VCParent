package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * Created by tacticalsk8er on 7/20/2014.
 */
public class VCGamemode extends ICommand {

    public VCGamemode(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Format: /gm (player) <gamemode>");
            return;
        }

        if (args.length == 1) {
            switch (args[0]) {
                case "survival":
                    player.setGameMode(GameMode.SURVIVAL);
                    Form.at(player, Prefix.SUCCESS, "You are in survival.");
                    break;
                case "creative":
                    player.setGameMode(GameMode.CREATIVE);
                    Form.at(player, Prefix.SUCCESS, "You are in creative.");
                    break;
                case "adventure":
                    player.setGameMode(GameMode.ADVENTURE);
                    Form.at(player, Prefix.SUCCESS, "You are in adventure.");
                    break;
                case "0":
                    player.setGameMode(GameMode.SURVIVAL);
                    Form.at(player, Prefix.SUCCESS, "You are in survival.");
                    break;
                case "1":
                    player.setGameMode(GameMode.CREATIVE);
                    Form.at(player, Prefix.SUCCESS, "You are in creative.");
                    break;
                case "2":
                    player.setGameMode(GameMode.ADVENTURE);
                    Form.at(player, Prefix.SUCCESS, "You are in adventure.");
                    break;
            }
        }

        if (args.length == 2) {
            Player player1 = Bukkit.getPlayer(args[0]);

            if (player1 == null) {
                Form.at(player, Prefix.ERROR, "No such player");
                return;
            }

            switch (args[1]) {
                case "survival":
                    player1.setGameMode(GameMode.SURVIVAL);
                    Form.at(player, Prefix.SUCCESS, player1.getName() + " is in survival.");
                    Form.at(player1, Prefix.SUCCESS, "You are in survival.");
                    break;
                case "creative":
                    player1.setGameMode(GameMode.CREATIVE);
                    Form.at(player, Prefix.SUCCESS, player1.getName() + " is in creative.");
                    Form.at(player1, Prefix.SUCCESS, "You are in creative.");
                    break;
                case "adventure":
                    player1.setGameMode(GameMode.ADVENTURE);
                    Form.at(player, Prefix.SUCCESS, player1.getName() + " is in adventure.");
                    Form.at(player1, Prefix.SUCCESS, "You are in adventure.");
                    break;
                case "0":
                    player1.setGameMode(GameMode.SURVIVAL);
                    Form.at(player, Prefix.SUCCESS, player1.getName() + " is in survival.");
                    Form.at(player1, Prefix.SUCCESS, "You are in survival.");
                    break;
                case "1":
                    player1.setGameMode(GameMode.CREATIVE);
                    Form.at(player, Prefix.SUCCESS, player1.getName() + " is in creative.");
                    Form.at(player1, Prefix.SUCCESS, "You are in creative.");
                    break;
                case "2":
                    player1.setGameMode(GameMode.ADVENTURE);
                    Form.at(player, Prefix.SUCCESS, player1.getName() + " is in adventure.");
                    Form.at(player1, Prefix.SUCCESS, "You are in adventure.");
                    break;
            }
        }
    }
}
