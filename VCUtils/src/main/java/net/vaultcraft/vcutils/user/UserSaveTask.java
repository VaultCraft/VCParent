package net.vaultcraft.vcutils.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.vaultcraft.vcutils.VCUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by tacticalsk8er on 8/22/2014.
 */
public class UserSaveTask extends BukkitRunnable {

    private String uuid;

    public UserSaveTask(String uuid) {
        this.uuid = uuid;
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
        Bukkit.getScheduler().runTaskAsynchronously(VCUtils.getInstance(), () -> {
            User.update(user);
        });
    }
}
