package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Connor Hollasch
 * @since 1/4/2015
 */
public class VCSetVoteCenter extends ICommand {

    public VCSetVoteCenter(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        Location l = player.getLocation();
        VCUtils.getInstance().getConfig().set("vote.center-location", l.getWorld().getName()+","+l.getX()+","+l.getY()+","+l.getZ()+","+l.getYaw()+","+l.getPitch());
        VCUtils.getInstance().saveConfig();

        Form.at(player, Prefix.SUCCESS, "Vote center location modified!");
    }
}
