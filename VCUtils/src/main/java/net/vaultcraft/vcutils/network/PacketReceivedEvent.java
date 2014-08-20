package net.vaultcraft.vcutils.network;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.io.ObjectInputStream;

/**
 * Created by tacticalsk8er on 8/19/2014.
 */
public class PacketReceivedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private String channel;
    private ObjectInputStream stream;

    public PacketReceivedEvent(String channel, ObjectInputStream stream) {
        this.channel = channel;
        this.stream = stream;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getChannel() {
        return channel;
    }

    public ObjectInputStream getStream() {
        return stream;
    }
}
