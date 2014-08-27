package common.network;

import net.vaultcraft.vcutils.network.VoteEvent;
import net.vaultcraft.vcutils.user.UUIDFetcher;
import org.bukkit.Bukkit;

import java.io.Serializable;
import java.net.Socket;
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
    public void run(Socket socket, String clientName) {
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
