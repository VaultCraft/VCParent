package net.vaultcraft.vcessentials.auction;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.string.StringUtils;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.entity.Player;

/**
 * @author Connor Hollasch
 * @since 11/2/2014
 */
public class VCAuction extends ICommand {

    public VCAuction(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
        subCmds.put("create", "Creates an auction with the item you're holding");
        subCmds.put("remove", "Used to remove one of your current auctions");
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            //Open the auction GUI
        }

        switch (args[0].toLowerCase()) {
            case "help": {
                Form.atHelp(player, this);
                return;
            }
            case "create": {
                processCreate(player, StringUtils.buildFromArray(args, 1).split(" "));
                return;
            }
            case "remove": {
                processRemove(player, StringUtils.buildFromArray(args, 1).split(" "));
                return;
            }
        }
    }

    private void processCreate(Player player, String[] args) {
        int live = AucManager.countPlayerAuctions(player);
        int max = AucManager.getMaxAuctions(User.fromPlayer(player).getGroup().getHighest());

        if (live >= max) {
            Form.at(player, Prefix.ERROR, "You cannot create more than ");
        }
    }

    private void processRemove(Player player, String[] args) {

    }
}
