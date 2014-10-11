package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

/**
 * Created by CraftFest on 10/11/2014.
 */
public class VCVote extends ICommand {

    public VCVote(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
            Form.at(player, Prefix.ERROR, "Voting is not yet released. Stay tuned!");
            return;
        }
}

