package net.vaultcraft.vcutils.innerplugin;

import com.google.common.collect.Lists;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Connor Hollasch
 * @since 10/12/14
 */
public class VCPluginManager {

    private static HashMap<String, InnerPlugin> plugins = new HashMap<>();

    public static void registerPlugin(InnerPlugin plugin, String name) {
        if (plugins.containsKey(name.toLowerCase())) {
            //Cannot support multiple plugin instances
            throw new UnsupportedOperationException("Cannot have two plugins of the same name");
        }
        plugins.put(name.toLowerCase(), plugin);
    }

    public static InnerPlugin getPlugin(String plugin) {
        return plugins.get(plugin.toLowerCase());
    }

    public static void register(Plugin plugin) {
        List<Class<?>> classes = getClassesInContainer(plugin);
        for (Class<?> c : classes) {
            if (InnerPlugin.class.isAssignableFrom(c)) {
                try {
                    InnerPlugin create = (InnerPlugin)c.newInstance();
                    registerPlugin(create, c.getSimpleName());
                    Logger.log(VCUtils.getInstance(), "Found inner plugin " + c.getSimpleName() + "! Adding to VCPluginManager");
                } catch (Exception ex) {
                    Logger.log(VCUtils.getInstance(), "Exception while enabling inner pluugin " + c.getSimpleName() + "!");
                }
            }
        }
    }

    public static ArrayList<Class<?>> getClassesInContainer(Plugin plugin) {
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        CodeSource codeSource = plugin.getClass().getProtectionDomain().getCodeSource();
        URL resource = codeSource.getLocation();
        String resPath = resource.getPath().replace("%20", " ");
        String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
        JarFile jFile;
        try {
            jFile = new JarFile(jarPath);
        }
        catch (IOException e) {
            throw new RuntimeException("Unexpected IOException reading JAR File '" + jarPath + "'", e);
        }
        Enumeration<JarEntry> entries = jFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            String className = null;
            if (entryName.endsWith(".class")) {
                className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
            }
            if (className != null) {
                Class<?> c = null;
                try {
                    c = Class.forName(className);
                }
                catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (c != null)
                    classes.add(c);
            }
        }
        try {
            jFile.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    public static HashMap<String, InnerPlugin> getPlugins() {
        return plugins;
    }
}
