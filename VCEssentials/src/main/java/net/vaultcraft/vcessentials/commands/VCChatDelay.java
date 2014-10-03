package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcessentials.listeners.VCChatListener;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

public class VCChatDelay extends ICommand {
    public VCChatDelay(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if(args.length < 1) {
            Form.at(player, Prefix.ERROR, "Format: /chatdelay <chat delay time in seconds>");
            return;
        }

        int time = Integer.parseInt(args[0]);
        VCChatListener.getInstance().delayTime = time;
        Form.at(player, Prefix.SUCCESS, "Chat delay has been set to "+time+" seconds.");
    }
}
