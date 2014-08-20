package common.network;

import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.logging.Logger;
import net.vaultcraft.vcutils.network.PacketReceivedEvent;
import org.bukkit.Bukkit;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * Created by tacticalsk8er on 8/19/2014.
 */
public class PacketOutSend implements Packet, Serializable {

    private String channel;
    private byte[] data;

    public PacketOutSend(PacketInSend packetInSend) {
        this.channel = packetInSend.getChannel();
        this.data = packetInSend.getData();
    }

    public PacketOutSend(PacketInSendAll packetInSendAll) {
        this.channel = packetInSendAll.getChannel();
        this.data = packetInSendAll.getData();
    }

    @Override
    public PacketOutSend getType() {
        return this;
    }

    @Override
    public void run(Socket socket, String clientName) {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new ByteArrayInputStream(data));
        } catch (IOException e) {
            Logger.error(VCUtils.getInstance(), e);
        }
        Bukkit.getPluginManager().callEvent(new PacketReceivedEvent(channel, in));
    }
}
