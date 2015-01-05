package net.vaultcraft.vcutils.util;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerVersioningUtils {

	public static boolean isv1_8(Player player) {
		if (((CraftPlayer) player).getHandle().playerConnection.networkManager.getVersion() > 5) {
			return true;
		}
		return false;
	}

}
