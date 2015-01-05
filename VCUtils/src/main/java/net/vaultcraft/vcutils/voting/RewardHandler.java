package net.vaultcraft.vcutils.voting;

import common.network.PacketInSendAll;
import net.md_5.bungee.api.ChatColor;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.network.MessageClient;
import net.vaultcraft.vcutils.title.TitleObject;
import net.vaultcraft.vcutils.user.OfflineUser;
import net.vaultcraft.vcutils.user.User;
import net.vaultcraft.vcutils.util.ConfigLocation;
import net.vaultcraft.vcutils.voting.listener.RewardListener;
import net.vaultcraft.vcutils.voting.rewards.VoteReward;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
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
    private static ConfigLocation vote_center_cl;

    private static VoteStation vote_center;

    public static RewardHandler me;

    public RewardHandler() {
        me = this;

        Bukkit.getPluginManager().registerEvents(this, VCUtils.getInstance());
        vote_center_cl = new ConfigLocation(VCUtils.getInstance(), "config.yml", "vote.center-location");
        vote_center = new VoteStation(vote_center_cl.getLocation());

        new RewardListener();
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

    public static VoteReward getRandomReward() {
        return rewards.get((int) (Math.random() * rewards.size()));
    }

    @EventHandler
    public void onVote(VoteEvent event) {
        User user = User.fromUUID(event.getUserUUID().toString());
        if(user == null) {
            OfflinePlayer exists = Bukkit.getOfflinePlayer(event.getUserUUID());
            if (!exists.hasPlayedBefore()) {
                return;
            }
            OfflineUser offline = OfflineUser.getOfflineUser(exists);
            offline.addTokens(1);
        }

        user.addTokens(1);
        Form.at(user.getPlayer(), Prefix.VOTE, "Thank you for your vote!");
        Form.at(user.getPlayer(), Prefix.VOTE, "You may now use this token at any vote station.");
        user.setLastVoted(System.currentTimeMillis());

        for (Player player : Bukkit.getOnlinePlayers()) {
            User _u = User.fromPlayer(player);
            if (_u.voted())
                continue;

            TitleObject to = new TitleObject(ChatColor.translateAlternateColorCodes('&', "&e&l" + user.getPlayer().getName() + " &d&l has voted!"),
                    ChatColor.translateAlternateColorCodes('&', "&7Vote to remove these messages. Type \"/vote\""));
            to.send(player);
        }
    }

    public ConfigLocation getVoteCenterConfiglocation() {
        return vote_center_cl;
    }

    public VoteStation getVoteStation() {
        return vote_center;
    }
}
