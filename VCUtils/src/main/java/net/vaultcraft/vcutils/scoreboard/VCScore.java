package net.vaultcraft.vcutils.scoreboard;

import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_7_R4.ScoreboardObjective;
import net.minecraft.server.v1_7_R4.ScoreboardScore;

/**
 * Created by tacticalsk8er on 9/2/2014.
 */
public class VCScore {

    private VCObjective objective;
    private String name;
    private int score;

    private VCTicker ticker;

    public VCScore(String name, int score, VCObjective objective) {
        this.name = name;
        this.score = score;
        this.objective = objective;
        for(VCScoreboard scoreboard : objective.getScoreboards()) {
            ScoreboardObjective scoreboardObjective = scoreboard.getScoreboard().getObjective(objective.getName());
            ScoreboardScore scoreboardScore = new ScoreboardScore(scoreboard.getScoreboard(), scoreboardObjective, name);
            scoreboardScore.setScore(score);
            PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(scoreboardScore, 0);
            scoreboard.sendPacket(packet);
        }
        objective.addScore(this);
    }

    public VCScore(VCTicker ticker, int score, VCObjective objective) {
        this(ticker.tick(), score, objective);
        this.ticker = ticker;
    }

    public void setName(String name) {
        for(VCScoreboard scoreboard : objective.getScoreboards()) {
            ScoreboardObjective scoreboardObjective = scoreboard.getScoreboard().getObjective(objective.getName());
            ScoreboardScore scoreboardScore = scoreboard.getScoreboard().getPlayerScoreForObjective(this.name, scoreboardObjective);
            ScoreboardScore newScoreboardScore = new ScoreboardScore(scoreboard.getScoreboard(), scoreboardObjective, name);
            newScoreboardScore.setScore(score);
            PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(scoreboardScore, 1);
            PacketPlayOutScoreboardScore packet1 = new PacketPlayOutScoreboardScore(newScoreboardScore, 0);
            scoreboard.sendPacket(packet);
            scoreboard.sendPacket(packet1);
        }
        this.name = name;
    }

    public void tick() {
        if (ticker == null)
            return;
        
        setName(ticker.tick());
    }

    public void setScore(int score) {
        for (VCScoreboard scoreboard : objective.getScoreboards()) {
            ScoreboardObjective scoreboardObjective = scoreboard.getScoreboard().getObjective(objective.getName());
            ScoreboardScore scoreboardScore = scoreboard.getScoreboard().getPlayerScoreForObjective(this.name, scoreboardObjective);
            scoreboardScore.setScore(score);
            PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(scoreboardScore, 0);
            scoreboard.sendPacket(packet);
        }
        this.score = score;
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
}
