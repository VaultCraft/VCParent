package net.vaultcraft.vcutils.user;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by tacticalsk8er on 8/3/2014.
 */
public class UserLoadedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private User user;

    public UserLoadedEvent(User user) {
        this.user = user;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public User getUser() {
        return user;
    }
}
