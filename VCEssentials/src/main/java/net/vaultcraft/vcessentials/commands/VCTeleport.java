package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by tacticalsk8er on 7/20/2014.
 */
public class VCTeleport extends ICommand {

    public VCTeleport() {
        super("tp", Group.MOD, "teleport");
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if(args.length == 0) {
            Form.at(player, Prefix.ERROR, "Format: /tp <playerFrom> <playerTo>");
            return;
        }

        if(args.length == 1) {
            Player player1 = Bukkit.getPlayerExact(args[0]);
            if(player1 == null) {
                Form.at(player, Prefix.ERROR, "Player: " + args[0] + " is not online.");
                return;
            }
            player.teleport(player1);
            return;
        }

        if(args.length == 2) {
            Player player1 = Bukkit.getPlayerExact(args[0]);
            Player player2 = Bukkit.getPlayerExact(args[1]);
            if(player1 == null) {
                Form.at(player, Prefix.ERROR, "Player: " + args[0] + " is not online.");
                return;
            }
            if(player2 == null) {
                Form.at(player, Prefix.ERROR, "Player: " + args[1] + " is not online.");
                return;
            }
            player1.teleport(player2);
            return;
        }

        if(args.length == 3) {
            try {
                int x = Integer.parseInt(args[0]);
                int y = Integer.parseInt(args[1]);
                int z = Integer.parseInt(args[2]);

                player.teleport(new Location(player.getWorld(), x, y, z));
            } catch (NumberFormatException e) {
                Form.at(player, Prefix.ERROR, "Arguments need to be integers.");
            }
        }
    }
}
