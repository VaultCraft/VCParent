package net.vaultcraft.vcutils.network;

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
    private boolean running = true;

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
            running = false;
            return;
        }
        while (running) {
            Packet packet;
            try {
                packet = (Packet) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                Logger.warning(VCUtils.getInstance(), "This server is no longer receiving messages from Message Server!");
                Logger.error(VCUtils.getInstance(), e);
                running = false;
                continue;
            }
            if(packet == null)
                continue;
            Logger.debug(VCUtils.getInstance(), "Message Received!");
            packet.run(client, VCUtils.uniqueServerName);
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
