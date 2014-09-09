package common.network;

import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.vaultcraft.vcutils.voting.VoteEvent;
import net.vaultcraft.vcutils.user.UUIDFetcher;
import org.bukkit.Bukkit;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by tacticalsk8er on 8/27/2014.
 */
public class PacketOutVote implements Packet, Serializable {

    private String username;

    public PacketOutVote(String username) {
        this.username = username;
    }

    @Override
    public PacketOutVote getType() {
        return this;
    }

    @Override
    public void run(ChannelHandlerContext chx, String clientName) {
        UUID userUUID = null;
        try {
            userUUID = UUIDFetcher.getUUIDOf(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userUUID != null)
            Bukkit.getPluginManager().callEvent(new VoteEvent(userUUID));
    }
}
