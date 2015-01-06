package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import net.vaultcraft.vcutils.voting.RewardHandler;
import org.bukkit.entity.Player;

/**
 * @author Connor Hollasch
 * @since 1/4/2015
 */
public class VCRedeem extends ICommand {

    public VCRedeem(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        if (!RewardHandler.me.getVoteStation().isUsable()) {
            Form.at(player, Prefix.VOTE, "There is no vote station on this server!");
            return;
        }

        User user = User.fromPlayer(player);
        if (user.getTokens() > 0) {
            if (player.getInventory().firstEmpty() == -1) {
                Form.at(player, Prefix.VOTE, "Not enough room in your inventory!");
                return;
            }

            player.getInventory().addItem(VCVote.vote_token);
            Form.at(player, Prefix.VOTE, "Redeemed a vote token!");
            user.setTokens(user.getTokens()-1);
            return;
        }

        Form.at(player, Prefix.VOTE, "You do not have any vote tokens!");
    }
}
