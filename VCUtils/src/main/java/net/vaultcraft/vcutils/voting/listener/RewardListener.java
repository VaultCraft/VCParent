package net.vaultcraft.vcutils.voting.listener;

import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.voting.RewardHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author Connor Hollasch
 * @since 12/29/2014
 */
public class RewardListener implements Listener {

    public RewardListener() {
        Bukkit.getPluginManager().registerEvents(this, VCUtils.getInstance());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (RewardHandler.me.getVoteStation().getInUse().equals(event.getPlayer()))
            RewardHandler.me.getVoteStation().onClick(event);

        else if (player.getItemInHand().getType().equals(Material.NETHER_STAR)) {
            //vote token...?
            ItemStack holding = player.getItemInHand();
            if (!(holding.hasItemMeta()))
                return;

            if (!(holding.getItemMeta().hasDisplayName()))
                return;

            if (RewardHandler.me.getVoteStation().isInUse()) {
                Form.at(player, Prefix.VOTE, "The vote station is already in use!");
                return;
            }

            //is vote token, I hope O_o
            removeOne(player, player.getInventory().getHeldItemSlot());
            RewardHandler.me.getVoteStation().beginPlayer(player);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlock().equals(event.getTo().getBlock()))
            return;

        if (RewardHandler.me.getVoteStation().getInUse().equals(event.getPlayer()))
            event.getPlayer().teleport(RewardHandler.me.getVoteStation().getCenter());
    }

    private static void removeOne(Player player, int slot) {
        Inventory inv = player.getInventory();
        ItemStack stack = inv.getItem(slot);
        if (stack == null)
            return;

        if (stack.getAmount() == 1)
            inv.setItem(slot, null);
        else {
            stack.setAmount(stack.getAmount() - 1);

            inv.setItem(slot, stack);
        }

        player.updateInventory();
    }
}
