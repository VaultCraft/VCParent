package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

/**
 * Created by tacticalsk8er on 7/27/2014.
 */
public class VCSpeed extends ICommand {
    public VCSpeed(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if(args.length == 0 || args.length == 1){
            Form.at(player, Prefix.ERROR, "Format: /speed <fly/walk> <speed>");
            return;
        }

        float speed;

        try {
            speed = Float.parseFloat(args[1]);
        } catch (NumberFormatException e) {
            Form.at(player, Prefix.ERROR, "Argument 2 needs to be a integer.");
            return;
        }

        switch (args[0]) {
            case "fly":
                player.setFlySpeed(speed);
                Form.at(player, Prefix.SUCCESS, "Fly speed: " + speed);
                break;
            case "walk":
                player.setWalkSpeed(speed);
                Form.at(player, Prefix.SUCCESS, "Walk speed: " + speed);
                break;
            default:
                Form.at(player, Prefix.ERROR, "Format: /speed <fly/walk> <speed>");
                break;
        }
    }
}
