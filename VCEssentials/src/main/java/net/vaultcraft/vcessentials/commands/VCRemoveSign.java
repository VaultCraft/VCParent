package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.sign.SignManager;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 * Created by Connor on 8/10/14. Designed for the VCPrison project.
 */

public class VCRemoveSign extends ICommand {

    public VCRemoveSign(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        if (args.length > 0) {
            String remove = args[0];
            if (SignManager.remove(remove)) {
                Form.at(player, Prefix.SUCCESS, "All signs matching meta: &e"+remove+Prefix.SUCCESS.getChatColor()+" removed!");
                return;
            }

            Form.at(player, Prefix.ERROR, "No possible meta to remove");
            return;
        }

        Block peek = player.getTargetBlock(null, 50);
        if (peek == null || peek.getType().equals(Material.AIR) || !(peek.getState() instanceof Sign)) {
            Form.at(player, Prefix.ERROR, "Please look at a sign!");
            return;
        }

        SignManager.remove(peek.getLocation());
        Form.at(player, Prefix.SUCCESS, "Sign removed in line of sight!");
    }
}
