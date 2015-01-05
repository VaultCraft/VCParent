package net.vaultcraft.vcutils.hologram;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftWitherSkull;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.util.Vector;

public class StaticHologramCraftEntityWitherSkull extends CraftWitherSkull {

	public StaticHologramCraftEntityWitherSkull(CraftServer server, StaticHologramNMSEntityWitherSkull entity) {
		super(server, entity);
	}

	@Override
	public void remove() {
		// Cannot be removed, this is the most important to override.

		StaticHologramNMSEntityWitherSkull skull = (StaticHologramNMSEntityWitherSkull) getHandle();
		if (skull.has_despawned) {
			entity.dead = true;
		}
	}

	// Method from Fireball
	@Override
	public void setDirection(Vector dir) {
	}

	// Method from Projectile
	@Override
	public void setBounce(boolean bounce) {
	}

	// Methods from Explosive
	@Override
	public void setYield(float yield) {
	}

	@Override
	public void setIsIncendiary(boolean fire) {
	}

	// Methods from Entity
	@Override
	public void setVelocity(Vector vel) {
	}

	@Override
	public boolean teleport(Location loc) {
		return false;
	}

	@Override
	public boolean teleport(Entity entity) {
		return false;
	}

	@Override
	public boolean teleport(Location loc, TeleportCause cause) {
		return false;
	}

	@Override
	public boolean teleport(Entity entity, TeleportCause cause) {
		return false;
	}

	@Override
	public void setFireTicks(int ticks) {
	}

	@Override
	public boolean setPassenger(Entity entity) {
		return false;
	}

	@Override
	public boolean eject() {
		return false;
	}

	@Override
	public boolean leaveVehicle() {
		return false;
	}

	@Override
	public void playEffect(EntityEffect effect) {
	}
}
