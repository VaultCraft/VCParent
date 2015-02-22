package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcutils.item.ItemUtils;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VCServerConfigItem implements ConfigurationSerializable
{
    Material material;
    String displayName;
    String server;
    List<String> lore;

    public VCServerConfigItem(Map<String, Object> map)
    {
        this.material = Material.getMaterial((String) map.get("material"));
        this.displayName = (String) map.get("displayName");
        this.server = (String) map.get("server");
        this.lore = (List<String>) map.get("lore");
    }

    public VCServerConfigItem(Material m, String displayName, String server, String... lore)
    {
        this.material = m;
        this.displayName = displayName;
        this.server = server;
        this.lore = Arrays.asList(lore);
    }


    public ItemStack toItemStack()
    {
        return ItemUtils.build(material, displayName, lore.toArray(new String[lore.size()]));
    }

    @Override
    public Map<String, Object> serialize() {
        return new LinkedHashMap<String, Object>() {{
            put("material", material.toString());
            put("displayName", displayName);
            put("server", server);
            put("lore", lore);
        }};
    }
}
