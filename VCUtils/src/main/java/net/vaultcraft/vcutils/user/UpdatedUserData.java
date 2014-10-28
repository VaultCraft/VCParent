package net.vaultcraft.vcutils.user;

import com.mongodb.DBObject;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by tacticalsk8er on 10/27/2014.
 */
public class UpdatedUserData implements Serializable {

    public String playerUUID;
    private String serverName;
    private String groups;
    private boolean banned;
    private Date tempBan;
    private boolean muted;
    private Date tempMute;

    private double money;
    private int tokens;

    private String prefix;

    //TODO Check if User is on same type of server.
    public UpdatedUserData(OfflineUser user, String serverName) {
        playerUUID = user.getPlayerUUID();
        this.serverName = serverName;
        groups = User.groupsToString(user.getGroup());
        banned = user.isBanned();
        tempBan = user.getTempBan();
        muted = user.isMuted();
        tempMute = user.getTempMute();
        money = user.getChangeInMoney();
        tokens = user.getChangeInTokens();
        prefix = user.getPrefix();
    }

    public void updateUser(User user) {
        List<Integer> groupLevels = User.parseGroups(groups);
        user.setGroup(Group.fromPermLevel(groupLevels.get(0)));
        groupLevels.remove(0);
        for(int i : groupLevels) {
            user.getGroup().merge(Group.fromPermLevel(i));
        }
        user.setBanned(banned, tempBan);
        if(banned)
            user.getPlayer().kickPlayer("You have been banned! You can post an appeal on our forums.");
        user.setMuted(muted, tempMute);
        if(VCUtils.serverName.equals(this.serverName)) {
            user.addMoney(money);
        } else {
            DBObject dbObject = User.getDBObject(UUID.fromString(playerUUID));
            double oldMoney = dbObject.get(serverName + "-Money") == null ? 0.0 : (double) dbObject.get(serverName + "-Money");
            oldMoney += money;
            dbObject.put(serverName + "-Money", oldMoney);
            DBObject dbObject1 = User.getDBObject(UUID.fromString(playerUUID));
            VCUtils.getInstance().getMongoDB().update(VCUtils.mongoDBName, "Users", dbObject1, dbObject);
        }
        user.addTokens(tokens);
        user.setPrefix(prefix);
    }
}
