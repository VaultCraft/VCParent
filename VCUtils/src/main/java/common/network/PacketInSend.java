package common.network;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * Created by tacticalsk8er on 8/19/2014.
 */
public class PacketInSend implements Packet, Serializable{

    private String serverName;
    private String channel;
    private byte[] data;

    public PacketInSend(String serverName, String channel, ByteArrayOutputStream out) {
        this.serverName = serverName;
        this.channel = channel;
        this.data = out.toByteArray();
    }

    public PacketInSend(String serverName, ByteArrayOutputStream out) {
        this(serverName, "", out);
    }

    @Override
    public PacketInSend getType() {
        return this;
    }

    @Override
    public void run(Socket socket, String clientName) {
        //Server Only
    }

    public String getChannel() {
        return channel;
    }

    public byte[] getData() {
        return data;
    }
}
