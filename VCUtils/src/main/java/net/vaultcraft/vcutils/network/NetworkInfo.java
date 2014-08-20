package net.vaultcraft.vcutils.network;

import net.vaultcraft.vcutils.config.ClassConfig;

/**
 * Created by tacticalsk8er on 8/19/2014.
 */
public class NetworkInfo {
    @ClassConfig.Config(path = "Network.Host")
    public static String host = "localhost";
    @ClassConfig.Config(path = "Network.Port")
    public static int port = 25566;
}
