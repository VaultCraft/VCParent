package net.vaultcraft.vcutils.network;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Created by tacticalsk8er on 8/27/2014.
 */
public class VoteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    UUID userUUID;

    public VoteEvent(UUID userUUID) {
        this.userUUID = userUUID;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public UUID getUserUUID() {
        return userUUID;
    }
}
