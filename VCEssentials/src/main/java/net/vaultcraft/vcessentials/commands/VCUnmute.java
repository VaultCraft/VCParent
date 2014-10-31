package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.OfflineUser;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * @author Nicholas Peterson
 */
public class VCUnmute extends ICommand {
    public VCUnmute(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if(args.length == 0) {
            Form.at(player, Prefix.ERROR, "Format: /unmute <player>");
            return;
        }

        Player player1 = Bukkit.getPlayer(args[0]);

        if(player1 == null) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if(offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                OfflineUser user = OfflineUser.getOfflineUser(offlinePlayer);
                user.setMuted(false, null);
                Form.at(player, Prefix.SUCCESS, offlinePlayer.getName() + " has been un-muted.");
                return;
            }
            Form.at(player, Prefix.ERROR, "Player does not exist.");
            return;
        }

        User user = User.fromPlayer(player1);
        user.setMuted(false, null);
        Form.at(player1, Prefix.VAULT_CRAFT, "You have been un-muted!");
        Form.at(player, Prefix.SUCCESS, player1.getName() + " has been un-muted.");
    }
}
