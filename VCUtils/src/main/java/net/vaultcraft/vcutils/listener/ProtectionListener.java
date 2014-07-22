package net.vaultcraft.vcutils.listener;

import net.vaultcraft.vcutils.protection.ProtectedArea;
import net.vaultcraft.vcutils.protection.ProtectionManager;
import net.vaultcraft.vcutils.protection.flag.FlagResult;
import net.vaultcraft.vcutils.protection.flag.FlagType;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Collection;

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

    private boolean willCancel(FlagType type, Player player, Location at) {
        if (player.isOp())
            return false;

        Group pGroup = User.fromPlayer(player).getGroup();
        FlagResult result = ProtectionManager.getInstance().getState(type, player, at);
        if (result.isCancelled())
            return (!(pGroup.hasPermission(result.getAllowed())));

        return false;
    }

    ///////////////////////////////////////////////////////
    //      EVENTS                                       //
    ///////////////////////////////////////////////////////

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location broken = event.getBlock().getLocation();

        event.setCancelled(willCancel(FlagType.BLOCK_BREAK, player, broken));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Location broken = event.getBlock().getLocation();

        event.setCancelled(willCancel(FlagType.BLOCK_PLACE, player, broken));
    }
}
