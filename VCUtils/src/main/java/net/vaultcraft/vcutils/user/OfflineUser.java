package net.vaultcraft.vcutils.user;

import org.bukkit.OfflinePlayer;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by tacticalsk8er on 10/26/2014.
 */
public class OfflineUser {


    private Group.GroupHandler group;

    private boolean banned = false;
    private Date tempBan = null;
    private boolean muted = false;
    private Date tempMute = null;

    private double money = 0;
    private int tokens = 0;

    private String prefix;

    private HashMap<String, String> globalUserdata = new HashMap<>();
    private HashMap<String, String> userdata = new HashMap<>();


    public OfflineUser(OfflinePlayer player) {

    }



}
