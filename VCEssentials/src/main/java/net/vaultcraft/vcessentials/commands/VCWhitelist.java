package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.WhitelistManager;
import org.bukkit.entity.Player;

/**
 * Created by Connor Hollasch on 9/24/14.
 */

public class VCWhitelist extends ICommand {

    public VCWhitelist(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Unknown sub command, format: /whitelist [on,off,add,remove,list] {args}");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "on": {
                WhitelistManager.setWhitelist(true);
                Form.at(player, Prefix.SUCCESS, "The whitelist is now &eon");
                return;
            }

            case "off": {
                WhitelistManager.setWhitelist(false);
                Form.at(player, Prefix.SUCCESS, "The whitelist is now &eoff");
                return;
            }

            case "add": {
                if (args.length == 1) {
                    Form.at(player, Prefix.ERROR, "Please specify a player to whitelist!");
                    return;
                }

                String add = args[1];
                if (WhitelistManager.getOnList().contains(add)) {
                    Form.at(player, Prefix.ERROR, "That player is already on the whitelist!");
                    return;
                }

                WhitelistManager.addPlayer(add);
                Form.at(player, Prefix.SUCCESS, "Player: &e" + add + Prefix.SUCCESS.getChatColor() + "added to the whitelist!");
                return;
            }

            case "remove": {
                if (args.length == 1) {
                    Form.at(player, Prefix.ERROR, "Please specify a player to un-whitelist!");
                    return;
                }

                String add = args[1];
                if (!WhitelistManager.getOnList().contains(add)) {
                    Form.at(player, Prefix.ERROR, "That player is not on the whitelist!");
                    return;
                }

                WhitelistManager.removePlayer(add);
                Form.at(player, Prefix.SUCCESS, "Player: &e" + add + Prefix.SUCCESS.getChatColor() + " removed from the whitelist!");
                return;
            }

            case "list": {
                Form.at(player, "Whitelist... " + WhitelistManager.getOnList());
                return;
            }
        }
    }
}
