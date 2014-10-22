package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.CommandManager;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.entity.Player;

public class VCCommands extends ICommand {
    public VCCommands(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        StringBuilder sb = new StringBuilder();
        for(ICommand command : CommandManager.getCommands().values()) {
            if(command.checkPerms(User.fromPlayer(player))) {
                sb.append(command.getName()).append(", ");
            }
        }
        if(sb.length() > 0) {
            sb.deleteCharAt(sb.lastIndexOf(", "));
        }
        Form.at(player, Prefix.VAULT_CRAFT, "You have access to the following commands: " + sb.toString());
    }
}
