package net.vaultcraft.vcutils.command;

import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.database.sql.MySQL;
import net.vaultcraft.vcutils.log.Logger;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class CommandManager implements Listener {

    public static CommandManager instance;
    private static HashMap<String, ICommand> commands = new HashMap<>();

    public static CommandManager getInstance() {
        return instance == null ? new CommandManager() : instance;
    }

    public CommandManager() {
        Bukkit.getPluginManager().registerEvents(this, VCUtils.getInstance());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPreprocessCommand(PlayerCommandPreprocessEvent event) {
        try {
            Player user = event.getPlayer();
            VCUtils.getInstance().mySQL.updateThread.add("INSERT INTO Commands VALUES(default, '"
                    + user.getUniqueId().toString() + "', '"
                    + user.getName() + "', '"
                    + User.fromPlayer(user).getGroup().getName() + "', '"
                    + event.getMessage() + "', "
                    + MySQL.getDate() + ")");
            //intercept command for vanilla override
            String command = event.getMessage().replace("/", "");
            String[] split = command.split(" ");
            String[] arguments = new String[split.length - 1];
            for (int x = 1; x < split.length; x++)
                arguments[x - 1] = split[x];

            processCommand(user, split[0], arguments);
            event.setCancelled(true);
        } catch (Exception ex) {
            Logger.exception(VCUtils.getInstance(), ex);
            event.setCancelled(true);
        }
    }

    private static void processCommand(Player player, String command, String[] args) {
        ICommand find = commands.get(command.toLowerCase());

        if (find != null) {
            boolean canUse = find.checkPerms(User.fromPlayer(player)) || player.isOp();

            if (!(canUse)) {
                Form.at(player, Prefix.ERROR, "You do not have permission to use this command!");
                return;
            }

            find.processCommand(player, args);
        } else {
            Form.at(player, Prefix.ERROR, "No such command!");
            return;
        }
    }

    public static void addCommand(ICommand cmd) {
        for (String alias : cmd.getAliases()) {
            commands.put(alias.toLowerCase(), cmd);
        }
        commands.put(cmd.getName(), cmd);
    }

    public static void removeCommand(ICommand cmd) {
        for (String alias : cmd.getAliases()) {
            commands.remove(alias.toLowerCase());
        }
        commands.remove(cmd.getName());
    }
}
