package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

/**
 * Created by tacticalsk8er on 7/20/2014.
 */
public class VCWeather extends ICommand {

    public VCWeather(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Format: /weather <rain/storm/clear> (seconds)");
            return;
        }

        if (args.length == 1) {
            switch (args[0]) {
                case "clear":
                    player.getWorld().setThundering(false);
                    player.getWorld().setStorm(false);
                    player.getWorld().setWeatherDuration(30000);
                    Form.at(player, Prefix.SUCCESS, "Weather set to clear.");
                    break;
                case "storm":
                    player.getWorld().setThundering(true);
                    player.getWorld().setStorm(true);
                    player.getWorld().setWeatherDuration(30000);
                    Form.at(player, Prefix.SUCCESS, "Weather set to storm.");
                    break;
                case "rain":
                    player.getWorld().setThundering(false);
                    player.getWorld().setStorm(true);
                    player.getWorld().setWeatherDuration(30000);
                    Form.at(player, Prefix.SUCCESS, "Weather set to rain.");
                    break;
                default:
                    Form.at(player, Prefix.ERROR, "Argument needs to be either rain, storm, or clear.");
                    break;
            }
            return;
        }

        if (args.length == 2) {
            try {
                int time = Integer.parseInt(args[0]);
                switch (args[0]) {
                    case "clear":
                        player.getWorld().setThundering(false);
                        player.getWorld().setStorm(false);
                        player.getWorld().setWeatherDuration(time * 20);
                        Form.at(player, Prefix.SUCCESS, "Weather set to clear.");
                        break;
                    case "storm":
                        player.getWorld().setThundering(true);
                        player.getWorld().setStorm(true);
                        player.getWorld().setWeatherDuration(time * 20);
                        Form.at(player, Prefix.SUCCESS, "Weather set to storm.");
                        break;
                    case "rain":
                        player.getWorld().setThundering(false);
                        player.getWorld().setStorm(true);
                        player.getWorld().setWeatherDuration(time * 20);
                        Form.at(player, Prefix.SUCCESS, "Weather set to rain.");
                        break;
                    default:
                        Form.at(player, Prefix.ERROR, "Argument 1 needs to be either rain, storm, or clear.");
                        break;
                }
            } catch (NumberFormatException e) {
                Form.at(player, Prefix.ERROR, "Argument 2 needs to be an integer.");
            }
        }
    }
}
