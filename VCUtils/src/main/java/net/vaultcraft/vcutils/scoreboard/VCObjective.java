package net.vaultcraft.vcutils.scoreboard;

import net.minecraft.server.v1_7_R4.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tacticalsk8er on 9/2/2014.
 */
public class VCObjective {

    private VCTicker ticker;

    private String name;
    private String oldName;
    private List<VCScoreboard> scoreboards = new ArrayList<>();
    private List<VCScore> scores = new ArrayList<>();

    public VCObjective(String name) {
        this.name = name;
    }

    public VCObjective(VCTicker ticker) {
        this(ticker.tick());
        this.ticker = ticker;
    }

    public void tick() {
        if (ticker != null)
            setName(ticker.tick());

        for (VCScore score : scores) {
            score.tick();
        }
    }

    public VCTicker getTicker() {
        return ticker;
    }

    public VCScore getFirstScore(int value) {
        for (VCScore score : scores) {
            if (score.getScore() == value)
                return score;
        }
        return null;
    }

    public void addScoreboard(VCScoreboard scoreboard) {
        ScoreboardObjective scoreboardObjective = scoreboard.getScoreboard().registerObjective(this.name, IScoreboardCriteria.b);
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective(scoreboardObjective, 0);
        scoreboard.sendPacket(packet);
        for(VCScore score : scores) {
            ScoreboardScore scoreboardScore = new ScoreboardScore(scoreboard.getScoreboard(), scoreboardObjective, score.getName());
            scoreboardScore.setScore(score.getScore());
            PacketPlayOutScoreboardScore packet1 = new PacketPlayOutScoreboardScore(scoreboardScore, 0);
            scoreboard.sendPacket(packet1);
        }
        scoreboards.add(scoreboard);
    }

    public void addScoreboardAndDisplay(VCScoreboard scoreboard, VCDisplay display) {
        ScoreboardObjective scoreboardObjective = scoreboard.getScoreboard().registerObjective(this.name, IScoreboardCriteria.b);
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective(scoreboardObjective, 0);
        scoreboard.sendPacket(packet);
        for(VCScore score : scores) {
            ScoreboardScore scoreboardScore = new ScoreboardScore(scoreboard.getScoreboard(), scoreboardObjective, score.getName());
            scoreboardScore.setScore(score.getScore());
            PacketPlayOutScoreboardScore packet1 = new PacketPlayOutScoreboardScore(scoreboardScore, 0);
            scoreboard.sendPacket(packet1);
        }
        PacketPlayOutScoreboardDisplayObjective packet2 = new PacketPlayOutScoreboardDisplayObjective(display.getId(), scoreboardObjective);
        scoreboard.sendPacket(packet2);
        scoreboards.add(scoreboard);
    }

    public void setName(String name) {
        for(VCScoreboard scoreboard : scoreboards) {
            ScoreboardObjective check = scoreboard.getScoreboard().getObjective(name);
            if(check != null)
                continue;

            ScoreboardObjective scoreboardObjective = scoreboard.getScoreboard().getObjective(this.name);
            if(scoreboardObjective == null)
                scoreboardObjective = scoreboard.getScoreboard().getObjective(this.oldName);
            if(scoreboardObjective == null)
                continue;
            scoreboardObjective.setDisplayName(name);
            PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective(scoreboardObjective, 2);
            scoreboard.sendPacket(packet);
        }
        this.oldName = this.name;
        this.name = name;
    }

    public void display(VCDisplay display) {
        for(VCScoreboard scoreboard : scoreboards) {
            ScoreboardObjective scoreboardObjective = scoreboard.getScoreboard().getObjective(this.name);
            PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective(display.getId(), scoreboardObjective);
            scoreboard.sendPacket(packet);
        }
    }

    public List<VCScoreboard> getScoreboards() {
        return scoreboards;
    }

    public String getName() {
        return name;
    }

    public String getOldName() {
        return oldName;
    }

    public void addScore(VCScore score) {
        if(scores.contains(score))
            return;
        scores.add(score);
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
}
