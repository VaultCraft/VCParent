package net.vaultcraft.vcutils.network;

import common.network.Packet;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.channel.ChannelInboundHandlerAdapter;
import net.minecraft.util.io.netty.channel.EventLoop;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.logging.Logger;

import java.util.concurrent.TimeUnit;

/**
 * Created by tacticalsk8er on 8/27/2014.
 */
public class MessageClientHandler extends ChannelInboundHandlerAdapter {
    private MessageClient client;

    public MessageClientHandler(MessageClient client) {
        this.client = client;
    }

    @Override
    public void channelRead(ChannelHandlerContext chx, Object msg)  {
        ((Packet)msg).run(chx, VCUtils.uniqueServerName);
        Logger.log(VCUtils.getInstance(), "Packet received.");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final EventLoop eventLoop = ctx.channel().eventLoop();
        Logger.log(VCUtils.getInstance(), "Lost connection to message server.");
        eventLoop.schedule(() -> client.init(), 1L, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext chx, Throwable cause) {
        cause.printStackTrace();
        chx.close();
    }
}
