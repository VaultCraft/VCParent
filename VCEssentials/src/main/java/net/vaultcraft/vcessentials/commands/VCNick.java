package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.entity.Player;

/**
 * @author Connor Hollasch
 * @since 10/27/2014
 */
public class VCNick extends ICommand {

    public VCNick(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Please specify a nickname to set your nickname or clear to clear your nickname");
            return;
        }

        User user = User.fromPlayer(player);

        if (args[0].equalsIgnoreCase("clear")) {
            user.setNick(null);

            Form.at(player, Prefix.SUCCESS, "Prefix removed!");
            return;
        }

        for (char c : args[0].toCharArray()) {
            if (!(Character.isLetterOrDigit(c)) && !(c == '_')) {
                Form.at(player, Prefix.ERROR, "Your nickname must be numbers and letters only!");
                return;
            }
        }

        user.setNick(args[0]);
        Form.at(player, Prefix.SUCCESS, "Your nickname was changed to &e " + args[0] + Prefix.SUCCESS.getChatColor()+"!");
    }
}
