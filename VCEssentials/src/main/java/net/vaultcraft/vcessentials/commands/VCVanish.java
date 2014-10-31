package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcessentials.VCEssentials;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import net.vaultcraft.vcutils.user.UserLoadedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tacticalsk8er on 10/29/2014.
 */
public class VCVanish extends ICommand implements Listener {

    List<String> vanishList = new ArrayList<>();

    public VCVanish(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
        Bukkit.getPluginManager().registerEvents(this, VCEssentials.getInstance());
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if(vanishList.contains(player.getName())) {
            vanishList.remove(player.getName());
            for(Player player1 : Bukkit.getOnlinePlayers()) {
                player1.showPlayer(player);
            }
            Form.at(player, Prefix.SUCCESS, "You are no longer invisible");
        } else {
            vanishList.add(player.getName());
            for(Player player1 : Bukkit.getOnlinePlayers()) {
                if(User.fromPlayer(player1).getGroup().hasPermission(Group.MOD))
                    continue;
                player1.hidePlayer(player);
            }
            Form.at(player, Prefix.SUCCESS, "You are now invisible");
        }
    }

    @EventHandler
    public void onJoin(UserLoadedEvent event) {
        if(event.getUser().getGroup().hasPermission(Group.MOD))
            return;
        for(String s : vanishList) {
            Player player = Bukkit.getPlayer(s);
            if(player == null)
                continue;
            event.getUser().getPlayer().hidePlayer(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if(vanishList.contains(event.getPlayer().getName())) {
            vanishList.remove(event.getPlayer().getName());
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.showPlayer(event.getPlayer());
            }
        }
    }
}
