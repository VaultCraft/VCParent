package net.vaultcraft.vcutils.network;

import common.network.Packet;
import net.minecraft.util.io.netty.bootstrap.Bootstrap;
import net.minecraft.util.io.netty.channel.*;
import net.minecraft.util.io.netty.channel.nio.NioEventLoopGroup;
import net.minecraft.util.io.netty.channel.socket.SocketChannel;
import net.minecraft.util.io.netty.channel.socket.nio.NioSocketChannel;
import net.minecraft.util.io.netty.handler.codec.serialization.ClassResolvers;
import net.minecraft.util.io.netty.handler.codec.serialization.ObjectDecoder;
import net.minecraft.util.io.netty.handler.codec.serialization.ObjectEncoder;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.logging.Logger;

import java.util.concurrent.TimeUnit;

/**
 * Created by tacticalsk8er on 8/27/2014.
 */
public class MessageClient {

    private String host;
    private int port;
    private static Channel serverChannel;
    private static EventLoopGroup workerGroup;

    public MessageClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public MessageClient init(){
        workerGroup = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new ObjectEncoder(), new ObjectDecoder(ClassResolvers.cacheDisabled(Packet.class.getClassLoader())), new MessageClientHandler(MessageClient.this));
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true);
        serverChannel = b.connect(host, port).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (!f.isSuccess()) {
                    Logger.log(VCUtils.getInstance(), "Could not connect to message server.");
                    f.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            init();
                        }
                    }, 1L, TimeUnit.SECONDS);
                } else {
                    Logger.log(VCUtils.getInstance(), "Connected to Message Server.");
                }
            }
        }).awaitUninterruptibly().channel();

            return this;
        }

    public static void sendPacket(Packet packet) {
        serverChannel.writeAndFlush(packet);
        Logger.log(VCUtils.getInstance(), "Packet sent");
    }

    public static void close() {
        serverChannel.closeFuture();
        workerGroup.shutdownGracefully();
    }
}
