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
public class VCGamemode extends ICommand{

    public VCGamemode(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if(args.length == 0) {
            Form.at(player, Prefix.ERROR, "Format: /gm <gamemode>");
            return;
        }

        if(args.length == 1) {
            switch (args[0]) {
                case "survival":
                    player.setGameMode(GameMode.SURVIVAL);
                    break;
                case "creative":
                    player.setGameMode(GameMode.CREATIVE);
                    break;
                case "adventure":
                    player.setGameMode(GameMode.ADVENTURE);
                    break;
                case "0":
                    player.setGameMode(GameMode.SURVIVAL);
                    break;
                case "1":
                    player.setGameMode(GameMode.CREATIVE);
                    break;
                case "2":
                    player.setGameMode(GameMode.ADVENTURE);
                    break;
            }
        }

        if(args.length == 2) {
            Player player1 = Bukkit.getPlayer(args[0]);

            if(player1 == null) {
                Form.at(player, Prefix.ERROR, "Player: " + args[0] + " is not online.");
                return;
            }

            switch (args[1]) {
                case "survival":
                    player1.setGameMode(GameMode.SURVIVAL);
                    break;
                case "creative":
                    player1.setGameMode(GameMode.CREATIVE);
                    break;
                case "adventure":
                    player1.setGameMode(GameMode.ADVENTURE);
                    break;
                case "0":
                    player1.setGameMode(GameMode.SURVIVAL);
                    break;
                case "1":
                    player1.setGameMode(GameMode.CREATIVE);
                    break;
                case "2":
                    player1.setGameMode(GameMode.ADVENTURE);
                    break;
            }
        }
    }
}
