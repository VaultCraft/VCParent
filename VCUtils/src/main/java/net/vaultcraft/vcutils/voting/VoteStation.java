package net.vaultcraft.vcutils.voting;

import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.hologram.StaticHologram;
import net.vaultcraft.vcutils.uncommon.Particles;
import net.vaultcraft.vcutils.voting.rewards.VoteReward;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Connor Hollasch
 * @since 12/29/2014
 */
public class VoteStation {

    private Location center;
    private Player user;

    private Block chestOne;
    private Block chestTwo;
    private Block chestThree;

    private boolean chestOneClicked = false;
    private boolean chestTwoClicked = false;
    private boolean chestThreeClicked = false;

    private int forceTaskId;

    private static HashMap<Block, MaterialData> old = new HashMap<>();

    public VoteStation(Location center) {
        this.center = center;

        chestOne = center.clone().add(0, 0, -2).getBlock();
        chestTwo = center.clone().add(2, 0, 0).getBlock();
        chestThree = center.clone().add(0, 0, 2).getBlock();
    }

    public boolean isInUse() {
        return user != null;
    }

    public Player getInUse() {
        return user;
    }

    public void beginPlayer(Player player) {
        user = player;

        player.teleport(center);

        //make gates
        Location gateStart = center.clone().add(-1, 0, 0);
        for (int x = 0; x < 4; x++) {
            final int _x = x;
            Runnable run = new Runnable() {
                public void run() {
                    Collection<Block> blocks = new HashSet<>();
                    blocks.add(gateStart.clone().add(0, _x, 2).getBlock());
                    blocks.add(gateStart.clone().add(0, _x, 1).getBlock());
                    blocks.add(gateStart.clone().add(0, _x, 0).getBlock());
                    blocks.add(gateStart.clone().add(0, _x, -1).getBlock());
                    blocks.add(gateStart.clone().add(0, _x, -2).getBlock());

                    for (Block b : blocks) {
                        old.put(b, new MaterialData(b.getType(), b.getData()));
                        b.setType(Material.IRON_FENCE);
                        b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getTypeId());
                    }
                }
            };
            Bukkit.getScheduler().scheduleSyncDelayedTask(VCUtils.getInstance(), run, x * 10);
        }

        forceTaskId = Bukkit.getScheduler().scheduleSyncDelayedTask(VCUtils.getInstance(), new Runnable() {
            public void run() {
                if (chestOneClicked && chestTwoClicked && chestThreeClicked)
                    return;

                if (!chestOneClicked) {
                    giveReward(chestOne);
                }
                if (!(chestTwoClicked)) {
                    giveReward(chestTwo);
                }
                if (!(chestThreeClicked)) {
                    giveReward(chestThree);
                }
            }
        }, 20 * 20);
    }

    private void end() {
        chestOneClicked = false;
        chestTwoClicked = false;
        chestThreeClicked = false;

        Bukkit.getScheduler().cancelTask(forceTaskId);
        forceTaskId = -1;
        user = null;

        for (Block b : old.keySet()) {
            MaterialData v = old.get(b);
            b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getTypeId());

            b.setTypeIdAndData(v.getItemTypeId(), v.getData(), true);
        }

        old.clear();
    }

    public void onClick(PlayerInteractEvent event) {
        Block b = event.getClickedBlock();
        if (b == null)
            return;

        if (b.equals(chestOne) && !chestOneClicked) {
            chestOneClicked = true;
            giveReward(chestOne);
        } else if (b.equals(chestTwo) && !chestTwoClicked) {
            chestTwoClicked = true;
            giveReward(chestTwo);
        } else if (b.equals(chestThree) && !chestThreeClicked) {
            chestThreeClicked = true;
            giveReward(chestThree);
        }
    }

    private void giveReward(Block chest) {
        int accumulate = 0;
        for (double i = 5.0; i >= 0; i-=0.25) {
            final double _i = i;
            final Location nxt = chest.getLocation().clone().add(.5, i, .5);
            Runnable play = new Runnable() {
                public void run() {
                    Particles.FIREWORKS_SPARK.sendToLocation(nxt, 0f, 0f, 0f, 0f, 1);
                    if (_i == 0) {
                        nxt.getWorld().playEffect(nxt, Effect.STEP_SOUND, Material.CHEST.getId());
                        nxt.getWorld().playSound(nxt, Sound.ANVIL_LAND, 1, 1);
                        VoteReward reward = RewardHandler.getRandomReward();
                        Item drop = nxt.getWorld().dropItem(nxt.clone().add(0, 1, 0), new ItemStack(reward.getIdentifier()));
                        drop.setVelocity(new Vector(0, 0, 0));
                        drop.setPickupDelay(20 * 60 * 60);

                        StaticHologram display = new StaticHologram(Arrays.asList(reward.reward(user)), chest.getLocation().clone().add(0, 1, 0));
                        display.respawnLines();

                        Runnable destroy = new Runnable() {
                            public void run() {
                                display.destroy();
                                drop.remove();

                                if (chestOneClicked && chestTwoClicked && chestThreeClicked) {
                                    end();
                                }
                            }
                        };
                        Bukkit.getScheduler().scheduleSyncDelayedTask(VCUtils.getInstance(), destroy, 20 * 5);
                    }
                }
            };
            Bukkit.getScheduler().scheduleSyncDelayedTask(VCUtils.getInstance(), play, accumulate+=2);
        }
    }

    public Location getCenter() {
        return center;
    }
}
