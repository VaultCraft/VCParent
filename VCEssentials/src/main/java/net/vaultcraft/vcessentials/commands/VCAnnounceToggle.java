package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcessentials.announce.AnnounceManager;
import net.vaultcraft.vcessentials.announce.AnnounceTask;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

/**
 * Created by Connor on 9/1/14. Designed for the VCUtils project.
 */

public class VCAnnounceToggle extends ICommand {

    public VCAnnounceToggle(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        AnnounceTask task = AnnounceManager.getTask(player);

        if (task._muted) {
            Form.at(player, Prefix.SUCCESS, "Announcements are no longer muted");
        } else {
            Form.at(player, Prefix.WARNING, "Muted announcements");
        }

        task._muted = !task._muted;
    }
}
