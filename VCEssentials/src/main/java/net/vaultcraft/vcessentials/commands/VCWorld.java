package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

/**
 * Created by tacticalsk8er on 8/13/2014.
 */
public class VCWorld extends ICommand{

    public VCWorld(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if(args.length == 0) {
            Form.at(player, Prefix.ERROR, "Missing Argument! World name.");
            return;
        }

        if (args[0].equalsIgnoreCase("create") && args.length > 1)
            Bukkit.createWorld(WorldCreator.name(args[0]));

        if(Bukkit.getWorld(args[0]) == null) {
            Form.at(player, Prefix.ERROR, "Invalid world.");
            return;
        }

        player.teleport(Bukkit.getWorld(args[0]).getSpawnLocation());
        Form.at(player, Prefix.SUCCESS, "You have teleported to " + args[0] + ".");
    }
}
