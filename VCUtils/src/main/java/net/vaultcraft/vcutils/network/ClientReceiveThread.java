package net.vaultcraft.vcutils.network;

import net.minecraft.util.org.apache.commons.io.IOUtils;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tacticalsk8er on 8/19/2014.
 */
public class ClientReceiveThread extends BukkitRunnable{

    private Socket client;
    private List<Callback> callbackList = new ArrayList<>();

    public ClientReceiveThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            Packet packet = (Packet) in.readObject();

            if(!packet.getChannel().isEmpty()) {
                for (int i = 0; i < callbackList.size(); i++) {
                    Callback callback = callbackList.get(i);
                    if (callback.getCommandType().equals(packet.getCommandType())) {
                        if (callback.getChannel().equals(packet.getChannel())) {
                            if(callback instanceof CallbackUser)
                                continue;
                            callback.callback(packet);
                            callbackList.remove(i);
                            break;
                        }
                    }
                }
            }

            switch (packet.getCommandType()) {
                case USER:
                    this.user(packet.getChannel(), packet.getDataStream());
                    break;
                case SEND:
                    final PacketReceivedEvent event = new PacketReceivedEvent(packet.getChannel(), packet.getDataStream());
                    Bukkit.getScheduler().runTask(VCUtils.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            Bukkit.getPluginManager().callEvent(event);
                        }
                    });
                    break;
            }
        } catch (IOException | ClassNotFoundException e){
            Logger.error(VCUtils.getInstance(), e);
        }
    }

    public void user(String channel, ObjectInputStream stream) {
        switch (channel) {
            case "get":
                try {
                    String uuid = stream.readUTF();
                    for(int i = 0; i < callbackList.size(); i++) {
                        if(!(callbackList.get(i) instanceof CallbackUser))
                            continue;
                        CallbackUser callbackUser = (CallbackUser) callbackList.get(i);
                        if(!callbackUser.getUuid().equals(uuid))
                            continue;
                        Packet packet = new Packet(Packet.CommandType.USER, "get", IOUtils.toByteArray(stream));
                        callbackUser.callback(packet);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public void addCallback(Callback callback) {
        callbackList.add(callback);
    }
}
