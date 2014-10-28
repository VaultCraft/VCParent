package net.vaultcraft.vcutils.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by tacticalsk8er on 10/27/2014.
 */
public class UpdatedUserData implements Serializable {

    public String playerUUID;
    private String groups;
    private boolean banned;
    private Date tempBan;
    private boolean muted;
    private Date tempMute;

    private double money;
    private int tokens;

    private String prefix;


    public UpdatedUserData(OfflineUser user) {
        playerUUID = user.getPlayerUUID();
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
        user.setMuted(muted, tempMute);
        user.addMoney(money);
        user.addTokens(tokens);
        user.setPrefix(prefix);
    }
}
