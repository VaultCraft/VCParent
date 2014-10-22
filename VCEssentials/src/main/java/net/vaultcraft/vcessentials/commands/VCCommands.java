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
        HashMap<Group, Integer> commandCount = new HashMap<>();

        for(Group g : Group.values()) {
            if(User.fromPlayer(player).getGroup().hasPermission(g)) {
                StringBuilder sb = new StringBuilder();
                sb.append(g.getChatColor()).append(ChatColor.BOLD.toString()).append(g.getName()).append(": ").append(ChatColor.RESET).append(g.getChatColor());
                commandLists.put(g, sb);
                commandCount.put(g, 0);
            }
        }
        for(ICommand command : CommandManager.getCommands().values()) {
            if(commandLists.keySet().contains(command.gerPermission())) {
                if(commandLists.get(command.gerPermission()).toString().contains(command.getName() + ", ")) {
                    continue;
                }
                commandLists.get(command.gerPermission()).append(command.getName()).append(", ");
                commandCount.put(command.gerPermission(), commandCount.get(command.gerPermission()) + 1);
            }
        }

        Form.at(player, Prefix.VAULT_CRAFT, "You have access to the following commands: ");
        for(Group g : commandLists.keySet()) {
            if(commandCount.get(g) <= 0) {
                continue;
            }
            StringBuilder theString = commandLists.get(g);
            if(theString.toString().contains(", ")) {
                theString.deleteCharAt(theString.lastIndexOf(", "));
            }
            if(theString.toString().endsWith(": "))
            player.sendMessage(theString.toString());
        }
    }
}
