package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

/**
 * Created by tacticalsk8er on 11/2/2014.
 */
public class VCCraft extends ICommand {

    public VCCraft(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        player.openWorkbench(null, true);
    }
}
