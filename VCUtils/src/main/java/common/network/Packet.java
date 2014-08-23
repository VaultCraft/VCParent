package common.network;

import java.io.Serializable;
import java.net.Socket;

/**
 * Created by tacticalsk8er on 8/18/2014.
 */
public interface Packet<T> extends Serializable{
    public T getType();

    public void run(Socket socket, String clientName);
}
