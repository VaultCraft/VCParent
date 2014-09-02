package net.vaultcraft.vcutils.scoreboard;

import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.Scoreboard;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created by tacticalsk8er on 9/2/2014.
 */
public class VCScoreboard {

    private Scoreboard scoreboard;
    private Player player;

    public VCScoreboard(Player player) {
        this.scoreboard = new Scoreboard();
        this.player = player;
        User.fromPlayer(player).setScoreboard(this);
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void sendPacket(Packet packet) {
        if(player.isOnline())
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
