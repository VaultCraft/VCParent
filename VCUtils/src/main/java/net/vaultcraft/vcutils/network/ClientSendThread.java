package net.vaultcraft.vcutils.network;

import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tacticalsk8er on 8/19/2014.
 */
public class ClientSendThread extends BukkitRunnable {

    private Socket client;

    private List<Packet> packets = new ArrayList<>();

    public ClientSendThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            while(true) {
                if(packets.size() > 0) {
                    Packet packet = packets.get(0);
                    out.writeObject(packet);
                    packets.remove(0);
                }
            }
        } catch (IOException e) {
            Logger.error(VCUtils.getInstance(), e);
        }
    }

    public void addPacket(Packet packet) {
        packets.add(packet);
    }
}
