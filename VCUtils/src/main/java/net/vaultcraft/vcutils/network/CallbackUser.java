package net.vaultcraft.vcutils.network;

/**
 * Created by tacticalsk8er on 8/19/2014.
 */
public abstract class CallbackUser extends Callback {

    private String uuid;

    public CallbackUser(Packet.CommandType commandType, String channel, String uuid) {
        super(commandType, channel);
        this.uuid = uuid;
    }

    public CallbackUser(Packet packet, String uuid) {
        super(packet);
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
