package net.vaultcraft.vcutils.scoreboard;

import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_7_R4.ScoreboardScore;

/**
 * Created by tacticalsk8er on 7/26/2014.
 */
public class VCScore {

    private String name;
    private int score;
    private VCObjective objective;

    public VCScore(String name, int score, VCObjective objective) {
        this.name = name;
        this.score = score;
        this.objective = objective;
        objective.registerScore(this);
    }

    public void setName(String name) {
        ScoreboardScore oldScore = this.getScoreboardScore(objective.getScoreboard(), objective);
        this.name = name;
        ScoreboardScore newScore = this.getScoreboardScore(objective.getScoreboard(), objective);
        PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(oldScore, 1);
        PacketPlayOutScoreboardScore packet1 = new PacketPlayOutScoreboardScore(newScore, 0);
        objective.getScoreboard().sendPacket(objective.getScoreboard().getPlayer(), packet);
        objective.getScoreboard().sendPacket(objective.getScoreboard().getPlayer(), packet1);
    }

    public void setScore(int score) {
        this.score = score;
        ScoreboardScore newScore = this.getScoreboardScore(objective.getScoreboard(), objective);
        PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(newScore, 0);
        objective.getScoreboard().sendPacket(objective.getScoreboard().getPlayer(), packet);
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public VCObjective getObjective() {
        return objective;
    }

    public ScoreboardScore getScoreboardScore(VCScoreboard scoreboard, VCObjective objective) {
        ScoreboardScore score =  new ScoreboardScore(scoreboard.getScoreboard(), objective.getObjective(), name);
        score.setScore(this.getScore());
        return score;
    }

    public void remove() {
        objective.unregisterScore(this);
    }
}
