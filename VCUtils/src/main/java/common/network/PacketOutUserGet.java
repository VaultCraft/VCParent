package common.network;

import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.vaultcraft.vcutils.user.User;

import java.io.Serializable;

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
    public void run(ChannelHandlerContext chx, String clientName) {
        User user = User.fromUUID(uuid);
        if (user != null)
            user.setUserInfo(info);
    }
}
