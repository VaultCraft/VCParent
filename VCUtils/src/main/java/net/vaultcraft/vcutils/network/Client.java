package net.vaultcraft.vcutils.network;

import common.network.Packet;
import common.network.PacketInStart;
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
    private ClientSendThread clientSendThread;
    private ClientReceiveThread clientReceiveThread;
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
        this.sendPacket(new PacketInStart(VCUtils.serverName));
    }

    public void sendPacket(Packet packet) {
        clientSendThread.addPacket(packet);
    }

    public void stop() {
        clientSendThread.setRunning(false);
        clientReceiveThread.setRunning(false);
        receiveTask.cancel();
        sendTask.cancel();
        try {
            client.close();
        } catch (IOException e) {
            Logger.error(VCUtils.getInstance(), e);
        }
    }
}
