package net.vaultcraft.vcutils.scoreboard;

import net.minecraft.server.v1_7_R4.IScoreboardCriteria;
import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_7_R4.ScoreboardObjective;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tacticalsk8er on 9/2/2014.
 */
public class VCObjective {

    private String name;
    private List<VCScoreboard> scoreboards = new ArrayList<>();

    public VCObjective(String name) {
        this.name = name;
    }

    public void addScoreboard(VCScoreboard scoreboard) {
        ScoreboardObjective scoreboardObjective = new ScoreboardObjective(scoreboard.getScoreboard(), name, IScoreboardCriteria.b);
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective(scoreboardObjective, 0);
        scoreboard.sendPacket(packet);
        scoreboards.add(scoreboard);
    }

    public void setName(String name) {
        for(VCScoreboard scoreboard : scoreboards) {
            ScoreboardObjective check = scoreboard.getScoreboard().getObjective(name);
            if(check != null)
                continue;
            ScoreboardObjective scoreboardObjective = scoreboard.getScoreboard().getObjective(this.name);
            scoreboardObjective.setDisplayName(name);
            PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective(scoreboardObjective, 2);
            scoreboard.sendPacket(packet);
        }
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
}
