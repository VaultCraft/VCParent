package net.vaultcraft.vcutils.scoreboard;

import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_7_R4.ScoreboardObjective;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tacticalsk8er on 7/26/2014.
 */
public class VCObjective {

    private String name;
    private VCCriteria criteria;
    private List<VCScore> scores = new ArrayList<>();
    private ScoreboardObjective objective;
    private VCScoreboard scoreboard;

    public VCObjective(String name, VCCriteria criteria, VCScoreboard scoreboard) {
        this.name = name;
        this.criteria = criteria;
        this.scoreboard = scoreboard;
        scoreboard.registerObjective(this);
    }

    public void setName(String name) {
        for(VCObjective objective1 : scoreboard.getObjectives())
            if(objective1.getName().equalsIgnoreCase(name))
                return;
        this.name = name;
        objective.setDisplayName(name);
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective(objective, 2);
        scoreboard.sendPacket(scoreboard.getPlayer(), packet);
    }

    public String getName() {
        return name;
    }

    public VCCriteria getCriteria() {
        return criteria;
    }

    public VCScore getScore(String name) {
        for(VCScore score : scores) {
            if(score.getName().equalsIgnoreCase(name))
                return score;
        }
        return null;
    }

    public List<VCScore> getScores() {
        return scores;
    }

    public VCScoreboard getScoreboard() {
        return scoreboard;
    }

    public void registerScore(VCScore score) {
        scores.add(score);
        PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(score.getScoreboardScore(scoreboard, this), 0);
        scoreboard.sendPacket(scoreboard.getPlayer(), packet);
    }

    public void unregisterScore(String name) {
        for(VCScore score : new ArrayList<>(scores)) {
            if (score.getName().equalsIgnoreCase(name)) {
                scores.remove(score);
                PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(score.getScoreboardScore(scoreboard, this), 1);
                scoreboard.sendPacket(scoreboard.getPlayer(), packet);
                break;
            }
        }
    }

    public void unregisterScore(VCScore score) {
        if(!scores.contains(score))
            return;
        scores.remove(score);
        PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(score.getScoreboardScore(scoreboard, this), 1);
        scoreboard.sendPacket(scoreboard.getPlayer(), packet);
    }

    public void setObjective(ScoreboardObjective objective) {
        this.objective = objective;
    }

    public ScoreboardObjective getObjective() {
        return objective;
    }

    public void remove() {
        scores.clear();
        scoreboard.unregisterObjective(this);
    }
}
