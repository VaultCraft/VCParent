package net.vaultcraft.vcutils.listener;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.protection.ProtectedArea;
import net.vaultcraft.vcutils.protection.ProtectionManager;
import net.vaultcraft.vcutils.protection.flag.FlagResult;
import net.vaultcraft.vcutils.protection.flag.FlagType;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

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
        FlagResult result = ProtectionManager.getInstance().getState(type, at);
        if (result.isCancelled())
            return (!(pGroup.hasPermission(result.getAllowed())));

        return false;
    }

    private boolean willCancel(FlagType type, Location at) {
        FlagResult result = ProtectionManager.getInstance().getState(type, at);
        if (result.isCancelled())
            return true;

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

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        Block block = event.getBlock();

        event.setCancelled(willCancel(FlagType.BLOCK_FORM, block.getLocation()));
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        Block block = event.getBlock();

        event.setCancelled(willCancel(FlagType.BLOCK_FROM_TO, block.getLocation()));
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();

        event.setCancelled(willCancel(FlagType.BLOCK_PHYSICS, block.getLocation()));
    }

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent event) {
        Block block = event.getBlock();

        event.setCancelled(willCancel(FlagType.LEAF_DECAY, block.getLocation()));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            event.setCancelled(willCancel(FlagType.ENTITY_DAMAGE, event.getEntity().getLocation()));
            return;
        }

        event.setCancelled(willCancel(FlagType.PLAYER_DAMAGE, event.getEntity().getLocation()));
    }

    @EventHandler
    public void onPlayerPvP(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            boolean will = willCancel(FlagType.PVP, event.getEntity().getLocation());
            if (will) {
                Form.at((Player) event.getDamager(), Prefix.ERROR, "You are in a no-pvp zone!");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        boolean will = willCancel(FlagType.CHAT, player, player.getLocation());
        if (will) {
            Form.at(player, Prefix.ERROR, "You are in a no-talk zone!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        boolean will = willCancel(FlagType.ITEM_DROP, player, player.getLocation());
        if (will) {
            Form.at(player, Prefix.ERROR, "You cannot drop items here!");
            event.setCancelled(true);
        }
    }
}
