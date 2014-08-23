package net.vaultcraft.vcutils.network;

import common.network.Packet;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by tacticalsk8er on 8/19/2014.
 */
public class ClientSendThread extends BukkitRunnable {

    private Socket client;

    private ConcurrentLinkedQueue<Packet> packets = new ConcurrentLinkedQueue<>();
    private boolean running = true;

    public ClientSendThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            while(running) {
                if(packets.size() > 0) {
                    Packet packet = packets.poll();
                    Logger.debug(VCUtils.getInstance(), packet.getClass().getSimpleName());
                    out.writeObject(packet);
                    Logger.debug(VCUtils.getInstance(), "Message Sent");
                }
            }
        } catch (IOException e) {
            Logger.warning(VCUtils.getInstance(), "This server is no longer sending messages from Message Server!");
            Logger.error(VCUtils.getInstance(), e);
            running = false;
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void addPacket(Packet packet) {
        packets.add(packet);
    }
}
