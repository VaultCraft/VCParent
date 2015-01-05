package net.vaultcraft.vcutils.util;

import java.lang.reflect.Field;
import java.util.Map;

public class ReflectionUtils {

	public static void putInPrivateStaticMap(Class clazz, String fieldName, Object key, Object value) throws Exception {
		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		Map map = (Map) field.get(null);
		map.put(key, value);
		field.set(null, map);
	}

}
