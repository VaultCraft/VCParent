package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Created by tacticalsk8er on 7/20/2014.
 */
public class VCTeleport extends ICommand {

    public VCTeleport(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if(args.length == 0) {
            Form.at(player, Prefix.ERROR, "Format: /tp <playerFrom> <playerTo> (x y z)");
            return;
        }

        if(args.length == 1) {
            Player player1 = Bukkit.getPlayer(args[0]);
            if(player1 == null) {
                Form.at(player, Prefix.ERROR, "No such player");
                return;
            }
            player.teleport(player1);
            player.playSound(player.getLocation(), Sound.FIREWORK_TWINKLE, 1, 1);
            Form.at(player, Prefix.SUCCESS, "You teleported to &e"+player1.getName()+Prefix.SUCCESS.getChatColor()+"!");
            return;
        }

        if(args.length == 2) {
            Player player1 = Bukkit.getPlayer(args[0]);
            Player player2 = Bukkit.getPlayer(args[1]);
            if(player1 == null) {
                Form.at(player, Prefix.ERROR, "No such player "+args[0]);
                return;
            }
            if(player2 == null) {
                Form.at(player, Prefix.ERROR, "No such player "+args[1
                        ]);
                return;
            }
            player1.teleport(player2);
            player.playSound(player.getLocation(), Sound.FIREWORK_TWINKLE, 1, 1);
            Form.at(player, Prefix.SUCCESS, "You teleported &e"+player1.getName()+Prefix.SUCCESS.getChatColor()+" to &e"+player2.getName()+Prefix.SUCCESS.getChatColor()+"!");
            return;
        }

        if(args.length == 3) {
            try {
                int x = Integer.parseInt(args[0]);
                int y = Integer.parseInt(args[1]);
                int z = Integer.parseInt(args[2]);

                player.teleport(new Location(player.getWorld(), x, y, z));
                player.playSound(player.getLocation(), Sound.FIREWORK_TWINKLE, 1, 1);
                String chatColor = Prefix.SUCCESS.getChatColor();
                Form.at(player, Prefix.SUCCESS, "You teleported to &e&n(X: "+x+", Y: "+y+", Z: "+z+")&r"+chatColor+"!");
            } catch (NumberFormatException e) {
                Form.at(player, Prefix.ERROR, "Arguments need to be integers.");
            }
        }
    }
}
