package net.vaultcraft.vcutils.voting;

import common.network.PacketInSendAll;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.network.MessageClient;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tacticalsk8er on 9/8/2014.
 */
public class RewardHandler implements Listener {

    private static List<VoteReward> rewards = new ArrayList<>();

    public RewardHandler() {
        Bukkit.getPluginManager().registerEvents(this, VCUtils.getInstance());
    }

    public static void registerReward(VoteReward reward) {
        rewards.add(reward);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(reward);
            objOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PacketInSendAll packet = new PacketInSendAll("Register-Reward", out);
        MessageClient.sendPacket(packet);
    }

    @EventHandler
    public void onVote(VoteEvent event) {
        User user = User.fromUUID(event.getUserUUID().toString());
        if(user == null) {

        }
        String rewards = user.getGlobalUserdata(VCUtils.serverName + "_rewards");

    }
}
