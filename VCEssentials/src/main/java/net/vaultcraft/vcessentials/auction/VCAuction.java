package net.vaultcraft.vcessentials.auction;

import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.string.StringUtils;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import net.vaultcraft.vcutils.util.DateUtil;
import net.vaultcraft.vcutils.util.SignGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Date;

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
            AucInv.open(player);
            return;
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
            Form.at(player, Prefix.ERROR, "You cannot create more than &e" + max + Prefix.ERROR.getChatColor() + " auctions!");
            return;
        }

        ItemStack holding = player.getItemInHand();
        SignGUI.SignGUIListener listener = (player1, lines) -> {
            if (lines[1] == null || lines[1].equals("")) {
                Form.at(player, Prefix.ERROR, "You didn't specify a time for the auction to last!");
                Form.at(player, Prefix.ERROR, "Time Format: y = year, mo = month, d = day, h = hour, m = minute, s = second. ");
                Form.at(player, Prefix.ERROR, "Example: 1h30m = 1 hour 30 minutes.");
                return;
            }

            if (lines[3] == null || lines[3].equals("")) {
                Form.at(player, Prefix.ERROR, "You didn't specify a minimum bid amount!");
                return;
            }

            Date d = new Date(DateUtil.parseDateDiff(lines[1], true));
            long diff = d.getTime() - System.currentTimeMillis();

            long maxDate = AucManager.getMaxCreationDuration(User.fromPlayer(player).getGroup().getHighest());

            if (diff > maxDate) {
                Form.at(player, Prefix.ERROR, "You cannot create an auction for more than " + (maxDate == (1000 * 60 * 5) ? "5 minutes" : (maxDate == (1000 * 60 * 60) ? "1 hour" :  "1 day")) + "!");
                return;
            }

            double minBid = 0.0;
            try {
                Double.parseDouble(lines[3].replace("$", ""));
            } catch (Exception ex) {
                Form.at(player, Prefix.ERROR, "Could not parse the time specified in the sign");
                return;
            }

            Auction auc = new Auction(player, holding, minBid);
            auc.setEndingDuration(System.currentTimeMillis() + diff);
            AucManager.createAuction(auc);
            AucManager.getStore().save();
        };
        VCUtils.getSignGUI().open(player, new String[]{"Auction time", "(e.g) 1d ", "Min bid amount", "$"}, listener);
    }

    private void processRemove(Player player, String[] args) {

    }
}
