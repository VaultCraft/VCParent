package net.vaultcraft.vcutils.scoreboard;

import net.minecraft.server.v1_7_R4.IScoreboardCriteria;

/**
 * Created by tacticalsk8er on 7/26/2014.
 */
public enum VCCriteria {
    DUMMY(IScoreboardCriteria.b),
    DEATHCOUNT(IScoreboardCriteria.c),
    PLAYERKILLCOUNT(IScoreboardCriteria.d),
    TOTALKILLCOUNT(IScoreboardCriteria.e),
    HEALTH(IScoreboardCriteria.f);

    private IScoreboardCriteria iScoreboardCriteria;

    private VCCriteria(IScoreboardCriteria iScoreboardCriteria) {
        this.iScoreboardCriteria = iScoreboardCriteria;
    }

    public IScoreboardCriteria getiScoreboardCriteria() {
        return iScoreboardCriteria;
    }
}
