package common.network;

import net.minecraft.util.io.netty.channel.ChannelHandlerContext;

/**
 * Created by tacticalsk8er on 8/29/2014.
 */
public class PacketInEnd implements Packet {
    @Override
    public PacketInEnd getType() {
        return this;
    }

    @Override
    public void run(ChannelHandlerContext chx, String clientName) {
        //Server Only
    }
}
