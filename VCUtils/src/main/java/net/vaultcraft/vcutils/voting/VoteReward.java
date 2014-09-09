package net.vaultcraft.vcutils.voting;

import java.io.Serializable;

/**
 * Created by tacticalsk8er on 9/8/2014.
 */
public abstract class VoteReward implements Serializable {

    String name;

    public VoteReward(String name) {
        this.name = name;
    }

    public boolean isUnlocked(String UUID) {
        return false;
    }

    public abstract void reward();
}
