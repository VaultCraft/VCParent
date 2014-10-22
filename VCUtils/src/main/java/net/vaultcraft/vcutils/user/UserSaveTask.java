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
            DBObject dbObject = VCUtils.getInstance().getMongoDB().query("VaultCraft", "Users", "UUID", user.getPlayer().getUniqueId().toString()) == null ? new BasicDBObject() : VCUtils.getInstance().getMongoDB().query("VaultCraft", "Users", "UUID", user.getPlayer().getUniqueId().toString());
            dbObject.put("UUID", user.getPlayer().getUniqueId().toString());
            dbObject.put("Group", User.groupsToString(user.getGroup()));
            dbObject.put("Banned", user.isBanned());
            dbObject.put("TempBan", user.getTempBan());
            dbObject.put("Muted", user.isMuted());
            dbObject.put("TempMute", user.getTempMute());
            dbObject.put(VCUtils.serverName + "-Money", user.getMoney());
            dbObject.put(VCUtils.serverName + "-UserData", user.getAllUserdata());
            dbObject.put("Tokens", user.getTokens());
            dbObject.put("Global-UserData", User.dataToString(user.getAllUserdata()));
            DBObject dbObject1 = VCUtils.getInstance().getMongoDB().query("VaultCraft", "Users", "UUID", user.getPlayer().getUniqueId().toString());
            if (dbObject1 == null)
                VCUtils.getInstance().getMongoDB().insert("VaultCraft", "Users", dbObject);
            else
                VCUtils.getInstance().getMongoDB().update("VaultCraft", "Users", dbObject1, dbObject);
        });
    }
}
