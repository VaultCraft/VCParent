package net.vaultcraft.vcutils.listener;

import net.vaultcraft.vcutils.protection.ProtectedArea;
import net.vaultcraft.vcutils.protection.ProtectionManager;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Created by Connor on 7/21/14. Designed for the VCUtils project.
 */

public class ProtectionListener implements Listener {

    private static ProtectionListener instance;

    public static ProtectionListener getInstance() {
        return instance;
    }

    public ProtectionListener() {
        instance = this;
    }

    ///////////////////////////////////////////////////////

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location broken = event.getBlock().getLocation();

        event.setCancelled(blockChange(player, broken));
    }

    private boolean blockChange(Player player, Location broken) {
        ProtectedArea pa = ProtectionManager.getInstance().fromLocation(broken);
        if (pa == null)
            return false;

        if (pa.doCancel(BlockBreakEvent.class)) {
            if (!(User.fromPlayer(player).getGroup().hasPermission(pa.getMembership())))
                return true;
        }
        return false;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Location broken = event.getBlock().getLocation();

        event.setCancelled(blockChange(player, broken));
    }
}
