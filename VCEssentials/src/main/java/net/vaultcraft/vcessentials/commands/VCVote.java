package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.item.ItemUtils;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import net.vaultcraft.vcutils.voting.VoteEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by CraftFest on 10/11/2014.
 */
public class VCVote extends ICommand {

    public static ItemStack vote_token = ItemUtils.build(Material.NETHER_STAR, "&e&lVote Token");

    public VCVote(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        Form.at(player, Prefix.ERROR, "Voting is not yet released. Stay tuned!");

        if (User.fromPlayer(player).getGroup().hasPermission(Group.MANAGER) && args.length > 0) {
            if (args[0].equalsIgnoreCase("givetoken")) {
                if (args.length > 1) {
                    Form.at(player, Prefix.ERROR, "Syntax /vote givetoken [player]");
                    return;
                }

                Player find = Bukkit.getPlayer(args[1]);

                if (find == null) {
                    Form.at(player, Prefix.ERROR, "No such player!");
                    return;
                }

                find.getInventory().addItem(vote_token);
                Form.at(find, Prefix.VOTE, "You received a vote token!");
                Form.at(player, Prefix.VOTE, "Gave &e" + find.getName() + Prefix.VOTE.getChatColor() + " a vote token!");
                return;
            } else if (args[0].equalsIgnoreCase("callvote")) {
                if (args.length > 1) {
                    Form.at(player, Prefix.ERROR, "Syntax /vote callvote [player]");
                    return;
                }

                Player find = Bukkit.getPlayer(args[1]);

                if (find == null) {
                    Form.at(player, Prefix.ERROR, "No such player!");
                    return;
                }

                VoteEvent ve = new VoteEvent(player.getUniqueId());
                Bukkit.getPluginManager().callEvent(ve);
                Form.at(player, Prefix.SUCCESS, "Vote event called!");
                return;
            }
        }

        return;
    }
}

