package net.vaultcraft.vcutils.util;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.vaultcraft.vcutils.VCUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author Nicholas Peterson
 */
public class BungeeUtil implements PluginMessageListener {

    private static HashMap<String, LinkedList<Response>> responseMap = new HashMap<>();

    public static void sendPlayerToServer(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);
        player.sendPluginMessage(VCUtils.getInstance(), "BungeeCord", out.toByteArray());
    }

    public static void serverPlayerCount(Player player, String serverName, Response response) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF(serverName);
        player.sendPluginMessage(VCUtils.getInstance(), "BungeeCord", out.toByteArray());
        LinkedList<Response> responses;
        if(!responseMap.containsKey("PlayerCount"))
            responses = new LinkedList<>();
        else
            responses = responseMap.get("PlayerCount");
        responses.add(response);
        responseMap.put("PlayerCount", responses);
    }

    public static void serverPlayerList(Player player, String serverName, Response response) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerList");
        out.writeUTF(serverName);
        player.sendPluginMessage(VCUtils.getInstance(), "BungeeCord", out.toByteArray());
        LinkedList<Response> responses;
        if(!responseMap.containsKey("PlayerList"))
            responses = new LinkedList<>();
        else
            responses = responseMap.get("PlayerList");
        responses.add(response);
        responseMap.put("PlayerList", responses);
    }

    public static void serverList(Player player, Response response) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServers");
        player.sendPluginMessage(VCUtils.getInstance(), "BungeeCord", out.toByteArray());
        LinkedList<Response> responses;
        if(!responseMap.containsKey("GetServers"))
            responses = new LinkedList<>();
        else
            responses = responseMap.get("GetServers");
        responses.add(response);
        responseMap.put("GetServers", responses);
    }

    @Override
    public void onPluginMessageReceived(String c, Player player, byte[] data) {
        if(!c.equalsIgnoreCase("bungeecord"))
            return;
        ByteArrayDataInput in = ByteStreams.newDataInput(data);
        String channel = in.readUTF();
        if(!responseMap.containsKey(channel))
            return;
        Response response = responseMap.get(channel).isEmpty() ? null : responseMap.get(channel).pop();
        if(response == null)
            return;
        response.response(in);
    }

    public interface Response {
        public void response(ByteArrayDataInput data);
    }
}
