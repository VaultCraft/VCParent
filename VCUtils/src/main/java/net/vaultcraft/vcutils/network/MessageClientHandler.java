package net.vaultcraft.vcutils.network;

import common.network.Packet;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.channel.ChannelInboundHandlerAdapter;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.logging.Logger;

/**
 * Created by tacticalsk8er on 8/27/2014.
 */
public class MessageClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext chx, Object msg)  {
        ((Packet)msg).run(chx, VCUtils.uniqueServerName);
        Logger.log(VCUtils.getInstance(), "Packet received.");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext chx) {
        MessageClient.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext chx, Throwable cause) {
        cause.printStackTrace();
        chx.close();
    }
}
