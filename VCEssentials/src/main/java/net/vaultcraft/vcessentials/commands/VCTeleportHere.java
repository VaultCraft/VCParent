package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Created by tacticalsk8er on 7/27/2014.
 */
public class VCTeleportHere extends ICommand {
    public VCTeleportHere(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Format: /tphere <player>");
            return;
        }

        Player player1 = Bukkit.getPlayer(args[0]);
        if (player1 == null) {
            Form.at(player, Prefix.ERROR, "No such player");
            return;
        }

        player1.teleport(player);
        player.playSound(player.getLocation(), Sound.FIREWORK_TWINKLE, 1, 1);
        Form.at(player, Prefix.SUCCESS, "You teleported &e" + player1.getName());
    }
}
