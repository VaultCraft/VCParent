package common.network;

import net.minecraft.util.io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

/**
 * Created by tacticalsk8er on 8/18/2014.
 */
public interface Packet<T> extends Serializable{
    public T getType();

    public void run(ChannelHandlerContext chx, String clientName);
}
