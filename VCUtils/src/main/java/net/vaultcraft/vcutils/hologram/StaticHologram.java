package net.vaultcraft.vcutils.hologram;

import net.minecraft.server.v1_7_R4.WorldServer;
import net.vaultcraft.vcutils.VCUtils;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class StaticHologram implements Listener {

	private List<String> lines = null;
	private Location location = null;
	private Set<StaticHologramNMSEntityHorse> horses = new HashSet<StaticHologramNMSEntityHorse>();
	private Set<StaticHologramNMSEntityWitherSkull> skulls = new HashSet<StaticHologramNMSEntityWitherSkull>();

	public Location getLocation() {
		return location;
	}

	public StaticHologram(List<String> lines, final Location location) {
		this.lines = lines;
		this.location = location;
		respawnLines();

		StaticHologramRegistry.me.getHOLOGRAMS().add(this);
	}

	public void respawnLines() {
		despawnLines();
		Location first = location.clone().add(0.0D, lines.size() / 2 * 0.23D, 0.0D);

		for (String line : lines) {
			spawnLine(first.clone(), (String) line);
			first.subtract(0.0D, 0.23D, 0.0D);
		}
	}

	private void despawnLines() {
		Iterator<StaticHologramNMSEntityWitherSkull> skulls_iterator = skulls.iterator();
		while (skulls_iterator.hasNext()) {
			StaticHologramNMSEntityWitherSkull skull = skulls_iterator.next();
			if (skull.isAlive()) {
				skull.despawn();
			}
			skulls_iterator.remove();
			StaticHologramRegistry.me.getENTITY_UUIDS().remove(skull.uniqueID.toString());
		}
		Iterator<StaticHologramNMSEntityHorse> horses_iterator = horses.iterator();
		while (horses_iterator.hasNext()) {
			StaticHologramNMSEntityHorse horse = horses_iterator.next();
			if (horse.isAlive()) {
				horse.despawn();
			}
			horses_iterator.remove();
			StaticHologramRegistry.me.getENTITY_UUIDS().remove(horse.uniqueID.toString());
		}
	}

	public void destroy() {
		despawnLines();
		StaticHologramRegistry.me.getHOLOGRAMS().remove(this);
	}

	private void spawnLine(Location location, String line) {
		WorldServer nmsWorld = ((CraftWorld) location.getWorld()).getHandle();

		StaticHologramNMSEntityHorse entity_horse = new StaticHologramNMSEntityHorse(nmsWorld);
		StaticHologramRegistry.me.getENTITY_UUIDS().add(entity_horse.getUniqueID().toString());
		entity_horse.setPosition(location.getX(), location.getY() + 55.0D, location.getZ());
		entity_horse.setHealth(20);
		entity_horse.setCustomName(line);
		entity_horse.setCustomNameVisible(true);

		StaticHologramNMSEntityWitherSkull wither_skull = new StaticHologramNMSEntityWitherSkull(nmsWorld);
		StaticHologramRegistry.me.getENTITY_UUIDS().add(wither_skull.getUniqueID().toString());
		wither_skull.setPosition(location.getX(), location.getY() + 55.0D, location.getZ());

		horses.add(entity_horse);
		skulls.add(wither_skull);

		nmsWorld.addEntity(entity_horse, SpawnReason.CUSTOM);
		nmsWorld.addEntity(wither_skull, SpawnReason.CUSTOM);

		entity_horse.setPassengerOf(wither_skull);
	}
}