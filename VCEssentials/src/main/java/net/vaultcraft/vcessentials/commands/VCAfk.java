package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcessentials.listeners.VCChatListener;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Sean on 10/22/2014.
 */
public class VCAfk extends ICommand {
    public VCAfk(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if(VCChatListener.afkPlayers.contains(player)) {
            VCChatListener.afkPlayers.remove(player);
            Form.at(player, Prefix.VAULT_CRAFT, "You are no longer AFK!");
        } else {
            VCChatListener.afkPlayers.add(player);
            Form.at(player, Prefix.VAULT_CRAFT, "You are now AFK!");
        }
    }
}
