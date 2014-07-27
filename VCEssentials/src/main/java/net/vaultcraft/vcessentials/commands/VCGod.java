package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tacticalsk8er on 7/21/2014.
 */
public class VCGod extends ICommand implements Listener {

    List<String> godMode = new ArrayList<>();

    public VCGod(Plugin plugin, String name, Group permission, String... aliases) {
        super(name, permission, aliases);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            if (godMode.contains(player.getName())) {
                godMode.remove(player.getName());
                Form.at(player, Prefix.SUCCESS, "God disabled.");
            } else {
                godMode.add(player.getName());
                Form.at(player, Prefix.SUCCESS, "God enabled.");
            }
            return;
        }

        if (args.length == 1) {
            Player player1 = Bukkit.getPlayer(args[0]);
            if (player1 == null) {
                Form.at(player, Prefix.ERROR, "No such player");
                return;
            }

            if (godMode.contains(player1.getName())) {
                godMode.remove(player1.getName());
                Form.at(player, Prefix.SUCCESS, "God disabled for " + player1.getName() + ".");
                Form.at(player1, Prefix.SUCCESS, "God disabled.");
            } else {
                godMode.add(player1.getName());
                Form.at(player, Prefix.SUCCESS, "God enabled for " + player1.getName() + ".");
                Form.at(player1, Prefix.SUCCESS, "God enabled.");
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (godMode.contains(((Player) e.getEntity()).getName())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if (godMode.contains(e.getEntity().getName())) {
            e.setCancelled(true);
        }
    }
}
