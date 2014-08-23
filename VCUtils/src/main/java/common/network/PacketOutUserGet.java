package common.network;

import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;

import java.io.Serializable;
import java.net.Socket;
import java.util.UUID;

/**
 * Created by tacticalsk8er on 8/19/2014.
 */
public class PacketOutUserGet implements Packet, Serializable {

    private String uuid;
    private UserInfo info;

    public PacketOutUserGet(String uuid, UserInfo info) {
        this.uuid = uuid;
        this.info = info;
    }

    @Override
    public PacketOutUserGet getType() {
        return this;
    }

    @Override
    public void run(Socket socket, String clientName) {
        User user = User.fromPlayer(Bukkit.getPlayer(UUID.fromString(uuid)));
        if (user != null)
            user.setUserInfo(info);
    }
}
