package net.vaultcraft.vcutils.network;

import common.network.CallbackPacket;
import common.network.Packet;
import common.network.PacketInStart;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tacticalsk8er on 8/19/2014.
 */
public class Client {

    private static List<CallbackPacket> callbackPackets = new ArrayList<>();

    private Socket client;
    private ClientSendThread clientSendThread;
    private BukkitTask receiveTask;
    private BukkitTask sendTask;

    public Client(String host, int port) {
        try {
            client = new Socket(host, port);
        } catch (IOException e) {
            Logger.error(VCUtils.getInstance(), e);
        }
        ClientReceiveThread clientReceiveThread = new ClientReceiveThread(client);
        clientSendThread = new ClientSendThread(client);
        receiveTask = clientReceiveThread.runTaskAsynchronously(VCUtils.getInstance());
        sendTask = clientSendThread.runTaskAsynchronously(VCUtils.getInstance());
        this.sendPacket(new PacketInStart(VCUtils.serverName));
    }

    public void sendPacket(Packet packet) {
        clientSendThread.addPacket(packet);
    }

    public static void addCallbackPacket(CallbackPacket callbackPacket) {
        callbackPackets.add(callbackPacket);
    }

    public static void removeCallbackPacket(CallbackPacket callbackPacket) {
        callbackPackets.remove(callbackPacket);
    }

    public static List<CallbackPacket> getAllCallbackPackets() {
        return callbackPackets;
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
