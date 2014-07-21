package net.vaultcraft.vcutils;

import net.vaultcraft.vcutils.command.CommandManager;
import net.vaultcraft.vcutils.listener.CommonPlayerListener;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Connor on 7/19/14. Designed for the VCUtils project.
 */

public class VCUtils extends JavaPlugin {

    private static VCUtils instance;

    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new CommandManager(), this);
        initListeners();

        for (Player player : Bukkit.getOnlinePlayers()) {
            CommonPlayerListener.getInstance().onPlayerJoin(new PlayerJoinEvent(player, null));
        }
    }

    public void onDisable() {

    }

    public void initListeners() {
        Bukkit.getPluginManager().registerEvents(new CommonPlayerListener(), this);
    }

    public static VCUtils getInstance() {
        return instance;
    }
}
