package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcessentials.listeners.VCChatListener;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.string.StringUtils;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;

/**
 * Created by Sean on 10/22/2014.
 */
public class VCMe extends ICommand {
    public VCMe(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if(args.length <= 0) {
            Form.at(player, Prefix.ERROR, "You need something to emote!");
            return;
        }

        VCChatListener.emotingPlayers.add(player);
        player.chat(StringUtils.buildFromArray(args));
    }
}
