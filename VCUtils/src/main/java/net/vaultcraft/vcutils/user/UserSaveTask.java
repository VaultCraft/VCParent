package net.vaultcraft.vcutils.user;

import common.network.PacketInSaveUser;
import common.network.UserInfo;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.network.MessageClient;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by tacticalsk8er on 8/22/2014.
 */
public class UserSaveTask extends BukkitRunnable {

    private String uuid;

    public UserSaveTask(String uuid) {
        this.uuid = uuid;
        this.runTaskTimer(VCUtils.getInstance(), 15 * 1200, 15 * 1200);
    }

    @Override
    public void run() {
        User user = User.fromUUID(uuid);
        if(user == null) {
            this.cancel();
            return;
        }
        if(!user.isReady())
            return;
        MessageClient.sendPacket(new PacketInSaveUser(new UserInfo(VCUtils.serverName, uuid), uuid, VCUtils.serverName));
    }
}
