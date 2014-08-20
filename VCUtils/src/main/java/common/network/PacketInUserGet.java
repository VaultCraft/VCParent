package common.network;

import java.io.Serializable;
import java.net.Socket;

/**
 * Created by tacticalsk8er on 8/19/2014.
 */
public abstract class PacketInUserGet implements CallbackPacket<PacketOutUserGet>, Serializable {

    private String uuid;
    private String serverName;

    public PacketInUserGet(String uuid, String serverName) {
        this.uuid = uuid;
        this.serverName = serverName;
    }

    @Override
    public PacketInUserGet getType() {
        return this;
    }

    @Override
    public void run(Socket socket, String clientName) {
        //Server Only
    }

    @Override
    public abstract void callback(PacketOutUserGet packet);
}
