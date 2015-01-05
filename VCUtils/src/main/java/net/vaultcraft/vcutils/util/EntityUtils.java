package net.vaultcraft.vcutils.util;

import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityTypes;

public class EntityUtils {

	// The id is the skin the entity will have.
	public static void injectCustomEntity(Class<? extends Entity> class_instance, String name, int id) throws Exception {
		ReflectionUtils.putInPrivateStaticMap(EntityTypes.class, "d", class_instance, name);
		ReflectionUtils.putInPrivateStaticMap(EntityTypes.class, "f", class_instance, id);
	}

}
