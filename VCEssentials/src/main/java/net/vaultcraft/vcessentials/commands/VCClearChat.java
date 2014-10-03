package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VCClearChat extends ICommand {
    public VCClearChat(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        for (int i = 0; i < 60; i++) {
            Bukkit.broadcastMessage("");
        }
        for(Player p : Bukkit.getOnlinePlayers()) {
            Form.at(p, Prefix.VAULT_CRAFT, "Chat was cleared by "+player.getDisplayName());
        }
    }
}
