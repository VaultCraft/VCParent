package common.network;

import net.minecraft.util.io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

/**
 * Created by tacticalsk8er on 8/19/2014.
 */
public class PacketInStart implements Packet, Serializable {

    private String name;

    public PacketInStart(String name) {
        this.name = name;
    }

    @Override
    public PacketInStart getType() {
        return this;
    }

    @Override
    public void run(ChannelHandlerContext chx, String clientName) {
        //Server Only
    }

    public String getName() {
        return name;
    }
}
