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
public class VCHeal extends ICommand {

    public VCHeal(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.setHealth(20d);
            player.setFoodLevel(20);
            player.setSaturation(12.8f);
            Form.at(player, Prefix.SUCCESS, "Healed!");
            return;
        }

        if (args.length == 1) {
            Player player1 = Bukkit.getPlayer(args[0]);
            if (player1 == null) {
                Form.at(player, Prefix.ERROR, "No such player");
                return;
            }
            player1.setHealth(20d);
            player1.setFoodLevel(20);
            player1.setSaturation(12.8f);
            Form.at(player, Prefix.SUCCESS, player1.getName() + " Healed!");
            Form.at(player1, Prefix.SUCCESS, "Healed!");
        }
    }
}
