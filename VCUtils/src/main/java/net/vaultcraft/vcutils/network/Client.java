package net.vaultcraft.vcutils.network;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by tacticalsk8er on 8/19/2014.
 */
public class Client {

    private Socket client;
    private ClientReceiveThread clientReceiveThread;
    private ClientSendThread clientSendThread;
    private BukkitTask receiveTask;
    private BukkitTask sendTask;

    public Client(String host, int port) {
        try {
            client = new Socket(host, port);
        } catch (IOException e) {
            Logger.error(VCUtils.getInstance(), e);
        }
        clientReceiveThread = new ClientReceiveThread(client);
        clientSendThread = new ClientSendThread(client);
        receiveTask = clientReceiveThread.runTaskAsynchronously(VCUtils.getInstance());
        sendTask = clientSendThread.runTaskAsynchronously(VCUtils.getInstance());
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(VCUtils.uniqueServerName);
        this.sendPacket(new Packet(Packet.CommandType.START, out.toByteArray()));
    }

    public void sendPacket(Packet packet) {
        clientSendThread.addPacket(packet);
    }

    public void sendPacket(Packet packet, Callback callback) {
        clientSendThread.addPacket(packet);
        clientReceiveThread.addCallback(callback);
    }

    public void stop() {
        receiveTask.cancel();
        sendTask.cancel();
        try {
            client.close();
        } catch (IOException e) {
            Logger.error(VCUtils.getInstance(), e);
        }
    }
}
