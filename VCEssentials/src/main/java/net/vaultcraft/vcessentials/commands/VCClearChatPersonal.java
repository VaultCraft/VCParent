package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

public class VCClearChatPersonal extends ICommand {
    public VCClearChatPersonal(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        for(int i = 0; i < 60; i++) {
            player.sendMessage("");
        }
        Form.at(player, Prefix.VAULT_CRAFT, "You cleared your chat box.");
    }
}
