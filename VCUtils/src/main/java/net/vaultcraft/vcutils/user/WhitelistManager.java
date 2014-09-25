package net.vaultcraft.vcutils.user;

import net.minecraft.util.com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Connor Hollasch on 9/24/14.
 */

public class WhitelistManager {

    private static boolean on = false;

    private static List<String> onList = Lists.newArrayList();

    public static void setWhitelist(boolean whitelist) {
        on = whitelist;
    }

    public static boolean isWhiteListed() {
        return on;
    }

    public static void addPlayer(String player) {
        onList.add(player);
    }

    public static void removePlayer(String player) {
        onList.remove(player);
    }

    public static List<String> getOnList() {
        return onList;
    }
}
