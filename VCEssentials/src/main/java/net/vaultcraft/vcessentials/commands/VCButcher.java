package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by tacticalsk8er on 7/30/2014.
 */
public class VCButcher extends ICommand {
    public VCButcher(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if(args.length == 0) {
            List<Entity> entities = player.getNearbyEntities(50, 50, 50);
            for(Entity entity : entities) {
                entity.remove();
            }
            Form.at(player, Prefix.SUCCESS, entities.size() + " entities were removed.");
            return;
        }

        int range = 50;
        try {
            range = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            Form.at(player, Prefix.ERROR, "Argument 1 needs to be an Integer.");
            return;
        }

        List<Entity> entities = player.getNearbyEntities(range, range, range);
        for(Entity entity : entities) {
            entity.remove();
        }
        Form.at(player, Prefix.SUCCESS, entities.size() + " entities were removed.");
    }
}
