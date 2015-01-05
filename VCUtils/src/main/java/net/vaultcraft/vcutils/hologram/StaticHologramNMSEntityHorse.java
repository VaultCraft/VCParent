package net.vaultcraft.vcutils.hologram;

import net.minecraft.server.v1_7_R4.EntityHorse;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.World;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;

public class StaticHologramNMSEntityHorse extends EntityHorse {
	public StaticHologramNMSEntityHorse(World world) {
		super(world);
		super.ageLocked = true;
		super.persistent = true;
		super.boundingBox.a = 0.0;
		super.boundingBox.b = 0.0;
		super.boundingBox.c = 0.0;
		super.boundingBox.d = 0.0;
		super.boundingBox.e = 0.0;
		super.boundingBox.f = 0.0;
		a(0.0F, 0.0F);
		setAge(-1700000); // This is a magic value. No one will see the real horse.
	}

	@Override
	public void h() {
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
	public void makeSound(String sound, float volume, float pitch) {
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
			this.bukkitEntity = new StaticHologramCraftEntityHorse(this.world.getServer(), this);
		}
		return this.bukkitEntity;
	}
}