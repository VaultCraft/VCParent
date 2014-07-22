package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

/**
 * Created by tacticalsk8er on 7/20/2014.
 */
public class VCTime extends ICommand{

    public VCTime(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if(args.length == 0) {
            Form.at(player, Prefix.ERROR, "Format: /time <ticks/day/night>");
            return;
        }

        if(args[0].equalsIgnoreCase("day")) {
            player.getWorld().setTime(6000);
            Form.at(player, Prefix.SUCCESS, "Time is set to day.");
            return;
        }

        if(args[0].equalsIgnoreCase("night")) {
            player.getWorld().setTime(18000);
            Form.at(player, Prefix.SUCCESS, "Time is set to night.");
            return;
        }

        try {
            long tick = Integer.parseInt(args[0]);
            player.getWorld().setTime(tick);
            Form.at(player, Prefix.SUCCESS, "Time is set to " + tick + ".");
        } catch (NumberFormatException e) {
            Form.at(player, Prefix.ERROR, "Argument needs to be an integer.");
        }
    }
}
