package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

/**
 * Created by tacticalsk8er on 7/27/2014.
 */
public class VCJump extends ICommand {
    public VCJump(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if(args.length == 0 || args.length == 1) {
            Form.at(player, Prefix.ERROR, "Format: /speed <fly/walk> <speed>");
            return;
        }

        float speed;

        try {
            speed = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            Form.at(player, Prefix.ERROR, "Argument 2 needs to be an integer.");
            return;
        }

        if (speed < 0 || speed > 10.0) {
            Form.at(player, Prefix.ERROR, "Speed must be between 1 and 10!");
            return;
        }

        switch (args[0]) {
            case "fly":
                player.setFlySpeed(speed/10f);
                Form.at(player, Prefix.SUCCESS, "Fly speed: " + speed);
                break;
            case "walk":
                player.setWalkSpeed(speed/10f);
                Form.at(player, Prefix.SUCCESS, "Walk speed: " + speed);
                break;
            default:
                Form.at(player, Prefix.ERROR, "Format: /speed <fly/walk> <speed>");
                break;
        }
    }
}
