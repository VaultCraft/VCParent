package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcessentials.VCEssentials;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

/**
 * Created by Connor on 9/1/14. Designed for the VCUtils project.
 */

public class VCHub extends ICommand {

    public VCHub(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    private static String[] lobby_names = {
        "hub"
    };

    public void processCommand(Player player, String[] args) {
        String transfer = lobby_names[(int)(Math.random()*lobby_names.length)];
        VCEssentials.getInstance().sendPlayerToServer(player, transfer);
    }
}
