package common.network;

import net.vaultcraft.vcutils.user.User;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by tacticalsk8er on 8/19/2014.
 */
public class UserInfo implements Serializable {

    private int group = 1;
    private boolean banned = false;
    private Date tempBan = null;
    private boolean muted = false;
    private Date tempMute = null;

    private double money = 0;
    private int tokens = 0;

    private HashMap<String, String> globalUserdata = new HashMap<>();
    private HashMap<String, String> userdata = new HashMap<>();

    public UserInfo(String serverName, String uuid) {
        User user = User.fromUUID(uuid);
        this.group = user.getGroup().getPermLevel();
        this.banned = user.isBanned();
        this.tempBan = user.getTempBan();
        this.muted = user.isMuted();
        this.tempMute = user.getTempMute();
        this.money = user.getMoney();
        this.tokens = user.getTokens();
        this.globalUserdata = user.getAllGlobalUserdata();
        this.userdata = user.getAllUserdata();
    }

    public void updateUser(String uuid, String serverName) {

    }

    public void saveUser(String uuid, String serverName) {

    }

    public int getGroup() {
        return group;
    }

    public boolean isBanned() {
        return banned;
    }

    public Date getTempBan() {
        return tempBan;
    }

    public boolean isMuted() {
        return muted;
    }

    public Date getTempMute() {
        return tempMute;
    }

    public double getMoney() {
        return money;
    }

    public int getTokens() {
        return tokens;
    }

    public HashMap<String, String> getGlobalUserdata() {
        return globalUserdata;
    }

    public HashMap<String, String> getUserdata() {
        return userdata;
    }
}