package net.vaultcraft.vcutils.voting.rewards;

import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by tacticalsk8er on 9/8/2014.
 */
public interface VoteReward {

    public Material getIdentifier();

    public String[] reward(Player player);
}
