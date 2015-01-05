package net.vaultcraft.vcutils.hologram;

import com.comphenix.protocol.ProtocolLibrary;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;

public class StaticHologramRegistry implements Listener {

	private static final HashSet<String> ENTITY_UUIDS = new HashSet<String>(); public HashSet<String> getENTITY_UUIDS() { return ENTITY_UUIDS; }
	private static final HashSet<StaticHologram> HOLOGRAMS = new HashSet<StaticHologram>(); public HashSet<StaticHologram> getHOLOGRAMS() { return HOLOGRAMS; }

	public static StaticHologramRegistry me;

	public StaticHologramRegistry() throws Exception {
		me = this;
		onEnable();
	}

	public void onEnable() throws Exception {
		try {
			EntityUtils.injectCustomEntity(StaticHologramNMSEntityHorse.class, "HologramEntityHorse", 100);
			EntityUtils.injectCustomEntity(StaticHologramNMSEntityWitherSkull.class, "HologramEntityWitherSkull", 19);
		} catch (Exception exception) {
			VCUtils.getInstance().getLogger().log(Level.SEVERE, "An error occured while injecting the custom entities in the hologram registry.");
		}

		ProtocolLibrary.getProtocolManager().addPacketListener(new StaticHologramPacketListener(this));
		Bukkit.getPluginManager().registerEvents(this, VCUtils.getInstance());
	}

	public boolean isStaticHologramEntity(org.bukkit.entity.Entity entity) {
		return ENTITY_UUIDS.contains(entity.getUniqueId().toString());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onCreatureSpawn(final CreatureSpawnEvent event) {
		if (event.isCancelled()) {
			if (ENTITY_UUIDS.contains(event.getEntity().getUniqueId().toString())) {
				event.setCancelled(false);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChunkLoad(final ChunkLoadEvent event) {
		Chunk chunk = event.getChunk();

		Iterator<StaticHologram> hologram_iterator = HOLOGRAMS.iterator();

		while (hologram_iterator.hasNext()) {
			StaticHologram hologram = hologram_iterator.next();

			Location location = hologram.getLocation();
			if (chunk.getX() == location.getChunk().getX() && chunk.getZ() == location.getChunk().getZ()) {
				hologram.respawnLines();
			}
		}
	}
}
