package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

/**
 * Created by Sean on 10/18/2014.
 */
public class VCUnban extends ICommand {

    private Plugin plugin;

    public VCUnban(Plugin plugin, String name, Group permission, String... aliases) {
        super(name, permission, aliases);
        this.plugin = plugin;
    }

    @Override
    public void processCommand(final Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Format: /unban <player uuid>");
        } else if(args.length == 1) {
            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(args[1]));
            final User theUser = new User(offlinePlayer.getPlayer());
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    theUser.setBanned(false, null);
                    User.remove(offlinePlayer.getPlayer());
                    Form.at(player, Prefix.SUCCESS, offlinePlayer.getName()+" has been successfully unbanned!");
                }
            }, 10);


        }

    }
}
