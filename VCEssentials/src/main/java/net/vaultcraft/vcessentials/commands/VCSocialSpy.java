package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tacticalsk8er on 10/28/2014.
 */
public class VCSocialSpy extends ICommand {

    private static List<String> spyList = new ArrayList<>();

    public VCSocialSpy(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if(isSpy(player)) {
            spyList.remove(player.getName());
            Form.at(player, Prefix.SUCCESS, "You are no longer spying.");
        } else {
            spyList.add(player.getName());
            Form.at(player, Prefix.SUCCESS, "You are now spying.");
        }
    }

    public static boolean isSpy(Player player) {
        return spyList.contains(player.getName());
    }

    public static List<String> getSpyList() {
        return spyList;
    }
}
