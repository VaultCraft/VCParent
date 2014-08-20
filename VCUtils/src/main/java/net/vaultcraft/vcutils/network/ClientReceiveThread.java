package net.vaultcraft.vcutils.network;

import common.network.CallbackPacket;
import common.network.Packet;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by tacticalsk8er on 8/19/2014.
 */
public class ClientReceiveThread extends BukkitRunnable {

    private Socket client;

    public ClientReceiveThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            Logger.error(VCUtils.getInstance(), e);
            return;
        }
        while (true) {
            if(!client.isConnected() || client.isInputShutdown() || client.isOutputShutdown() || client.isClosed()) {
                break;
            }

            Packet packet;
            try {
                packet = (Packet) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                Logger.error(VCUtils.getInstance(), e);
                continue;
            }
            if(packet instanceof CallbackPacket)
                Client.addCallbackPacket((CallbackPacket) packet);
            packet.run(client, VCUtils.uniqueServerName);
            Logger.debug(VCUtils.getInstance(), "Message Received!");
        }
    }
}
