package net.vaultcraft.vcutils.network;

/**
 * Created by tacticalsk8er on 8/19/2014.
 */
public abstract class Callback {

    private Packet.CommandType commandType;
    private String channel;

    public Callback(Packet.CommandType commandType, String channel) {
        this.commandType = commandType;
        this.channel = channel;
    }

    public Callback(Packet packet) {
        this.commandType = packet.getCommandType();
        this.channel = packet.getChannel();
    }

    public abstract void callback(Packet packet);

    public Packet.CommandType getCommandType() {
        return commandType;
    }

    public String getChannel() {
        return channel;
    }
}
