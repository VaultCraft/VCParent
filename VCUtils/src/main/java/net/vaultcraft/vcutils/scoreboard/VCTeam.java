package net.vaultcraft.vcutils.scoreboard;

import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_7_R4.ScoreboardTeam;
import net.vaultcraft.vcutils.VCUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Nicholas Peterson
 */
public class VCTeam {

    private String name;
    private VCObjective objective;
    private String prefix;
    private String suffix;
    private List<String> members;
    private boolean friendlyFire;
    private boolean canSeeFriendlyInvisibles;
    private TeamRunnable runnable;
    private BukkitTask task;

    public VCTeam(VCObjective objective, String name, String prefix, String suffix, boolean friendlyFire, boolean canSeeFriendlyInvisibles, String... members) {
        if (objective == null)
            throw new IllegalArgumentException("Objective cannot be null.");
        if (prefix.length() > 16)
            throw new IllegalArgumentException("Prefix cannot be longer than 16 characters!");
        if (suffix.length() > 16)
            throw new IllegalArgumentException("Suffix cannot be longer than 16 characters!");

        this.name = name;
        this.objective = objective;
        this.prefix = prefix;
        this.suffix = suffix;
        this.members = new ArrayList<>(Arrays.asList(members));
        this.friendlyFire = friendlyFire;
        this.canSeeFriendlyInvisibles = canSeeFriendlyInvisibles;
        this.runnable = new TeamRunnable();
        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(VCUtils.getInstance(), runnable, 120, 120);

        for (VCScoreboard vcScoreboard : objective.getScoreboards()) {
            ScoreboardTeam team = new ScoreboardTeam(vcScoreboard.getScoreboard(), this.name);
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam(team, 0);
            vcScoreboard.sendPacket(packet);
        }

    }

    public void setPrefix(String prefix) {
        if (prefix.length() > 16)
            throw new IllegalArgumentException("Prefix cannot be longer than 16 characters!");
        this.prefix = prefix;

        if (objective == null)
            return;

        for (VCScoreboard vcScoreboard : objective.getScoreboards()) {
            ScoreboardTeam team = vcScoreboard.getScoreboard().getTeam(this.name);
            team.setPrefix(this.prefix);
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam(team, 2);
            vcScoreboard.sendPacket(packet);
        }
    }

    public void setSuffix(String suffix) {
        if (suffix.length() > 16)
            throw new IllegalArgumentException("Suffix cannot be longer than 16 characters!");
        this.suffix = suffix;

        if (objective == null)
            return;

        for (VCScoreboard vcScoreboard : objective.getScoreboards()) {
            ScoreboardTeam team = vcScoreboard.getScoreboard().getTeam(this.name);
            team.setSuffix(this.suffix);
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam(team, 2);
            vcScoreboard.sendPacket(packet);
        }
    }

    public void addMember(Player player) {
        if (!members.contains(player.getName())) {
            members.add(player.getName());
            if (!runnable.addMembers.contains(player.getName()))
                runnable.addMembers.add(player.getName());
        }
    }

    public void removeMember(Player player) {
        if (members.contains(player.getName())) {
            members.remove(player.getName());
            if (!runnable.removeMembers.contains(player.getName()))
                runnable.removeMembers.add(player.getName());
        }
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;

        if (objective == null)
            return;

        for (VCScoreboard vcScoreboard : objective.getScoreboards()) {
            ScoreboardTeam team = vcScoreboard.getScoreboard().getTeam(this.name);
            team.setAllowFriendlyFire(this.friendlyFire);
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam(team, 2);
            vcScoreboard.sendPacket(packet);
        }
    }

    public void setCanSeeFriendlyInvisibles(boolean canSeeFriendlyInvisibles) {
        this.canSeeFriendlyInvisibles = canSeeFriendlyInvisibles;

        if (objective == null)
            return;

        for (VCScoreboard vcScoreboard : objective.getScoreboards()) {
            ScoreboardTeam team = vcScoreboard.getScoreboard().getTeam(this.name);
            team.setCanSeeFriendlyInvisibles(this.canSeeFriendlyInvisibles);
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam(team, 2);
            vcScoreboard.sendPacket(packet);
        }
    }

    public void remove() {
        if (objective == null)
            return;
        for (VCScoreboard vcScoreboard : this.objective.getScoreboards()) {
            ScoreboardTeam team = vcScoreboard.getScoreboard().getTeam(this.name);
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam(team, 1);
            vcScoreboard.sendPacket(packet);
        }

        objective = null;
    }

    private class TeamRunnable implements Runnable {

        private List<String> addMembers = new ArrayList<>();
        private List<String> removeMembers = new ArrayList<>();

        @Override
        public void run() {
            if (addMembers.size() > 0) {
                for (VCScoreboard vcScoreboard : objective.getScoreboards()) {
                    ScoreboardTeam team = vcScoreboard.getScoreboard().getTeam(name);
                    team.getPlayerNameSet().add(addMembers);
                    addMembers.clear();
                    PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam(team, 3);
                    vcScoreboard.sendPacket(packet);
                }
            }

            if (removeMembers.size() > 0) {
                for (VCScoreboard vcScoreboard : objective.getScoreboards()) {
                    ScoreboardTeam team = vcScoreboard.getScoreboard().getTeam(name);
                    team.getPlayerNameSet().remove(removeMembers);
                    removeMembers.clear();
                    PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam(team, 4);
                    vcScoreboard.sendPacket(packet);
                }
            }
        }
    }
}
