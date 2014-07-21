package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by tacticalsk8er on 7/20/2014.
 */
public class VCFly extends ICommand{

    public VCFly(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {

        if(args.length == 0) {
            player.setAllowFlight(!player.getAllowFlight());
        }

        if(args.length == 0) {
            Player player1 = Bukkit.getPlayer(args[0]);
            if(player1 == null) {
                Form.at(player, Prefix.ERROR, "Player: " + args[0] + " is not online.");
                return;
            }
            player1.setAllowFlight(!player1.getAllowFlight());
        }
    }
}
