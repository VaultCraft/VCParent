package net.vaultcraft.vcutils.hologram;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.packetwrapper.*;
import net.vaultcraft.vcutils.util.PlayerVersioningUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Iterator;
import java.util.List;

public class StaticHologramPacketListener extends PacketAdapter {
	private static final double VERTICAL_OFFSET = 56.8;

	private final StaticHologramRegistry registry;

	public StaticHologramPacketListener(StaticHologramRegistry registry) {
		super(VCUtils.getInstance(), ListenerPriority.HIGHEST, PacketType.Play.Server.ATTACH_ENTITY, PacketType.Play.Server.SPAWN_ENTITY_LIVING, PacketType.Play.Server.SPAWN_ENTITY, PacketType.Play.Server.ENTITY_METADATA, PacketType.Play.Server.ENTITY_TELEPORT);

		this.registry = registry;
	}

	@Override
	public void onPacketSending(PacketEvent event) {
		if (!PlayerVersioningUtils.isv1_8(event.getPlayer())) {
			return;
		}

		if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY) {
			WrapperPlayServerSpawnEntity spawnPacket = new WrapperPlayServerSpawnEntity(event.getPacket());
			Entity entity = spawnPacket.getEntity(event);
			if (entity == null) {
				return;
			}

			// We need to replace it with items.
			if (entity.getType() == EntityType.WITHER_SKULL && registry.isStaticHologramEntity(entity)) {
				spawnPacket.setEntityID(1);
			}
		} else if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
			WrapperPlayServerSpawnEntityLiving spawnLivingPacket = new WrapperPlayServerSpawnEntityLiving(event.getPacket());
			Entity entity = spawnLivingPacket.getEntity(event);
			if (entity == null) {
				return;
			}
			if (spawnLivingPacket.getType() == EntityType.HORSE && registry.isStaticHologramEntity(entity)) {
				spawnLivingPacket.setY(spawnLivingPacket.getY() - VERTICAL_OFFSET);
				spawnLivingPacket.setType(30); // Armor stands
				List<WrappedWatchableObject> metadata = spawnLivingPacket.getMetadata().getWatchableObjects();
				if (metadata != null) {
					pruneUselessIndexes(metadata);
					// Invisible.
					metadata.add(new WrappedWatchableObject(0, (byte) 0x20));
					spawnLivingPacket.setMetadata(new WrappedDataWatcher(metadata));
				}
			}
		} else if (event.getPacketType() == PacketType.Play.Server.ATTACH_ENTITY) {
			WrapperPlayServerAttachEntity attachPacket = new WrapperPlayServerAttachEntity(event.getPacket());
			Entity vehicle = attachPacket.getVehicle(event);
			if (vehicle != null && vehicle.getType() == EntityType.WITHER_SKULL && registry.isStaticHologramEntity(vehicle)) {
				Entity passenger = attachPacket.getEntity(event);
				if (passenger != null && passenger.getType() == EntityType.HORSE) {
					event.setCancelled(true); // Do not send attach packets
				}
			}
		} else if (event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA) {
			WrapperPlayServerEntityMetadata metadataPacket = new WrapperPlayServerEntityMetadata(event.getPacket());
			Entity entity = metadataPacket.getEntity(event);
			if (entity == null) {
				return;
			}
			if (entity.getType() == EntityType.HORSE && registry.isStaticHologramEntity(entity)) {
				// The horse metadata is applied to the wither skull instead
				List<WrappedWatchableObject> metadata = metadataPacket.getEntityMetadata();
				pruneUselessIndexes(metadata);
				metadataPacket.setEntityMetadata(metadata);
			}
		} else if (event.getPacketType() == PacketType.Play.Server.ENTITY_TELEPORT) {
			WrapperPlayServerEntityTeleport teleportPacket = new WrapperPlayServerEntityTeleport(event.getPacket());
			Entity entity = teleportPacket.getEntity(event);
			if (entity == null) {
				return;
			}
			if (entity.getType() == EntityType.WITHER_SKULL && entity.getPassenger() != null && entity.getPassenger().getType() == EntityType.HORSE && registry.isStaticHologramEntity(entity)) {
				teleportPacket.setEntityID(entity.getPassenger().getEntityId());
				teleportPacket.setY(teleportPacket.getY() - VERTICAL_OFFSET);
			}
		}
	}

	private void pruneUselessIndexes(List<WrappedWatchableObject> metadata) {
		Iterator<WrappedWatchableObject> iter = metadata.iterator();
		while (iter.hasNext()) {
			int index = iter.next().getIndex();
			if (index != 2 && index != 3) {
				iter.remove();
			}
		}
	}
}