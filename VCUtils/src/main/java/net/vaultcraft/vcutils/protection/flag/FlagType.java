package net.vaultcraft.vcutils.protection.flag;

import org.bukkit.event.Event;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;

/**
 * Created by Connor on 7/21/14. Designed for the VCUtils project.
 */

public enum FlagType {

    BLOCK_BREAK(BlockBreakEvent.class, "blockbreak", "break", "breakblock", "destroy"),
    BLOCK_PLACE(BlockPlaceEvent.class, "blockplace", "place", "placeblock", "construct"),
    BLOCK_FORM(BlockFormEvent.class, "blockform", "form", "formblock"),
    BLOCK_FROM_TO(BlockFromToEvent.class, "blockfromto", "fromto"),
    BLOCK_PHYSICS(BlockPhysicsEvent.class, "blockphysics", "physicsblock", "physics"),
    LEAF_DECAY(LeavesDecayEvent.class, "leafdecay", "decayleaf"),
    PLAYER_DAMAGE(EntityDamageEvent.class, "playerdamage", "damage", "damageplayer", "invincible"),
    ENTITY_DAMAGE(EntityDamageEvent.class, "entitydamage"),
    PVP(EntityDamageByEntityEvent.class, "pvp", "entitydamagebyentity", "playervsplayer"),
    CHAT(AsyncPlayerChatEvent.class, "playerchat", "chat", "chatplayer", "talk"),
    ITEM_DROP(PlayerDropItemEvent.class, "dropitem", "itemdrop", "playerdropitem"),
    CREATURE_SPAWN(CreatureSpawnEvent.class, "entityspawn", "mobspawn", "mobspawning"),
    HUNGER(FoodLevelChangeEvent.class, "food", "hunger", "foodchange"),
    BUCKET_EMPTY(PlayerBucketEmptyEvent.class, "bucketempty", "emptybucket", "bucketdrain"),
    BUCKET_FILL(PlayerBucketFillEvent.class, "bucketfill", "fillbucket"),
    HANGING_PLACE(HangingPlaceEvent.class, "hanging-place", "hangingplace", "paintingplace", "itemframeplace"),
    BLOCK_SPREAD(BlockSpreadEvent.class, "spread", "firespread");

    private String[] aliases;

    private FlagType(Class<? extends Event> event, String... aliases) {
        this.aliases = aliases;
    }

    public String[] getAliases() {
        return aliases;
    }

    public static FlagType fromString(String input) {
        input = input.toLowerCase().replace("-", "").replace("_", "").replace(" ", "");
        for (FlagType t : values()) {
            for (String a : t.getAliases()) {
                if (a.toLowerCase().startsWith(input))
                    return t;
            }
        }
        return null;
    }
}
