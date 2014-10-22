package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.CommandManager;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class VCCommands extends ICommand {
    public VCCommands(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        HashMap<Group, StringBuilder> commandLists = new HashMap<>();

        for(Group g : Group.values()) {
            if(User.fromPlayer(player).getGroup().hasPermission(g)) {
                StringBuilder sb = new StringBuilder();
                sb.append(g.getChatColor()).append(ChatColor.BOLD.toString()).append(g.getName()).append(": ");
                commandLists.put(g, sb);
            }
        }
        for(ICommand command : CommandManager.getCommands().values()) {
            if(commandLists.keySet().contains(command.gerPermission())) {
                commandLists.get(command.gerPermission()).append(command.getName()).append(", ");
            }
        }

        Form.at(player, Prefix.VAULT_CRAFT, "You have access to the following commands: ");
        for(Group g : commandLists.keySet()) {
            StringBuilder theString = commandLists.get(g);
            theString.deleteCharAt(theString.lastIndexOf(", "));
            player.sendMessage(theString.toString());
        }
    }
}
