package net.vaultcraft.vcutils.scoreboard;

import net.minecraft.server.v1_7_R3.Packet;
import net.minecraft.server.v1_7_R3.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_7_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_7_R3.Scoreboard;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tacticalsk8er on 7/26/2014.
 */
public class VCScoreboard {

    private Scoreboard scoreboard;
    private Player player;
    private List<VCObjective> objectives = new ArrayList<>();

    public VCScoreboard(Player player) {
        scoreboard = new Scoreboard();
        this.player = player;
        User.fromPlayer(player).setScoreboard(this);

    }

    public void registerObjective(VCObjective objective) {
        for (VCObjective objective1 : objectives)
            if (objective1.getName().equalsIgnoreCase(objective.getName()))
                return;

        objective.setObjective(scoreboard.registerObjective(objective.getName(), objective.getCriteria().getiScoreboardCriteria()));
        objectives.add(objective);
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective(objective.getObjective(), 0);
        this.sendPacket(player, packet);
    }

    public void unregisterObjective(String name) {
        for (VCObjective objective : new ArrayList<>(objectives)) {
            if (objective.getName().equalsIgnoreCase(name)) {
                scoreboard.unregisterObjective(objective.getObjective());
                objectives.remove(objective);
                PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective(objective.getObjective(), 1);
                this.sendPacket(player, packet);
            }
        }
    }

    public void unregisterObjective(VCObjective objective) {
        scoreboard.unregisterObjective(objective.getObjective());
        objectives.remove(objective);
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective(objective.getObjective(), 1);
        this.sendPacket(player, packet);
    }

    public void removeAllObjectives() {
        for (VCObjective objective : objectives) {
            objective.getScores().clear();
            scoreboard.unregisterObjective(objective.getObjective());
            PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective(objective.getObjective(), 1);
            this.sendPacket(player, packet);
        }

        objectives.clear();
    }

    public void displayObjective(VCObjective objective, VCDisplay display) {
        PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective(display.getId(), objective.getObjective());
        sendPacket(player, packet);
    }

    public void removeDisplay(VCDisplay display) {
        PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective(display.getId(), null);
        sendPacket(player, packet);
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Player getPlayer() {
        return player;
    }

    public VCObjective getObjective(String name) {
        for (VCObjective objective : objectives) {
            if (objective.getName().equalsIgnoreCase(name)) {
                return objective;
            }
        }
        return null;
    }

    public List<VCObjective> getObjectives() {
        return objectives;
    }

    public void sendPacket(Player player, Packet packet) {
        if (player.isOnline())
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public void remove() {
        this.removeAllObjectives();
        player = null;
        scoreboard = null;
    }
}
