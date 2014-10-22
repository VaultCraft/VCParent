package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.CommandManager;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VCCommands extends ICommand {
    public VCCommands(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        HashMap<Group, StringBuilder> commandLists = new HashMap<>();
        List<Group> commandGroups = new ArrayList<>();

        for(Group g : Group.values()) {
            if(User.fromPlayer(player).getGroup().hasPermission(g)) {
                StringBuilder sb = new StringBuilder();
                sb.append(g.getChatColor()).append(ChatColor.BOLD.toString()).append(g.getName()).append(": ").append(ChatColor.RESET).append(g.getChatColor());
                commandLists.put(g, sb);
            }
        }
        for(ICommand command : CommandManager.getCommands().values()) {
            if(commandLists.keySet().contains(command.gerPermission())) {
                commandLists.get(command.gerPermission()).append(command.getName()).append(", ");
                if(!commandGroups.contains(command.gerPermission())) {
                    commandGroups.add(command.gerPermission());
                }
            }
        }

        Form.at(player, Prefix.VAULT_CRAFT, "You have access to the following commands: ");
        for(Group g : commandGroups) {
            StringBuilder theString = commandLists.get(g);
            if(theString.toString().contains(", ")) {
                theString.deleteCharAt(theString.lastIndexOf(", "));
            }
            if(theString.toString().endsWith(": "))
            player.sendMessage(theString.toString());
        }
    }
}
