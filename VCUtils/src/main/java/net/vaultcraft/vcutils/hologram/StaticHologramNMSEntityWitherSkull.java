package net.vaultcraft.vcutils.hologram;

import net.minecraft.server.v1_7_R4.EntityWitherSkull;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.World;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;

public class StaticHologramNMSEntityWitherSkull extends EntityWitherSkull {
	public StaticHologramNMSEntityWitherSkull(World world) {
		super(world);
		super.motX = 0.0;
		super.motY = 0.0;
		super.motZ = 0.0;
		super.dirX = 0.0;
		super.dirY = 0.0;
		super.dirZ = 0.0;
		super.boundingBox.a = 0.0;
		super.boundingBox.b = 0.0;
		super.boundingBox.c = 0.0;
		super.boundingBox.d = 0.0;
		super.boundingBox.e = 0.0;
		super.boundingBox.f = 0.0;
		super.attachedToPlayer = false;
		a(0.0F, 0.0F);
	}

	@Override
	public void b(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
	}

	@Override
	public boolean c(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
		return false;
	}

	@Override
	public boolean d(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
		return false;
	}

	@Override
	public void e(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
	}

	@Override
	public void h() {
	}

	@Override
	public void makeSound(String sound, float f1, float f2) {
		// Remove sounds.
	}

	public boolean has_despawned = false;

	@Override
	public void die() {
		if (has_despawned) {
			super.die();
		}
	}

	public void despawn() {
		has_despawned = true;
		die();
	}

	@Override
	public CraftEntity getBukkitEntity() {
		if (super.bukkitEntity == null) {
			this.bukkitEntity = new StaticHologramCraftEntityWitherSkull(this.world.getServer(), this);
		}
		return this.bukkitEntity;
	}
}