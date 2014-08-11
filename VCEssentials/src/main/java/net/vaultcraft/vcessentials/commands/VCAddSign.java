package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.sign.SignLoader;
import net.vaultcraft.vcutils.sign.SignManager;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;import java.lang.String;

/**
 * Created by Connor on 8/8/14. Designed for the VCPrison project.
 */

public class VCAddSign extends ICommand {

    public VCAddSign(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Please specify a meta tag!");
            return;
        }

        Location at = player.getTargetBlock(null, 50).getLocation();
        if (at == null || (!(at.getBlock().getState() instanceof Sign))) {
            Form.at(player, Prefix.ERROR, "Please look at a sign to add!");
            return;
        }

        Sign sign = (Sign)at.getBlock().getState();
        SignManager.addSign(sign.getLocation(), args[0]);

        SignLoader.getInstance().save();
        Form.at(player, Prefix.SUCCESS, "Sign added with meta: &e"+args[0]+Prefix.SUCCESS.getChatColor()+"!");
    }
}
