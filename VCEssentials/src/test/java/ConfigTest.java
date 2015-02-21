import net.vaultcraft.vcessentials.commands.VCServerConfigItem;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

public class ConfigTest {

    @Test
    public void ServerConfigTest() {
        YamlConfiguration testConfig = new YamlConfiguration();
        LinkedList<VCServerConfigItem> list = new LinkedList<>();
        list.add(new VCServerConfigItem(Material.ICE, "Some Name", "a server", "some lore", "or two"));
        list.add(new VCServerConfigItem(Material.WOOD, "Some Name again", "a server 1", "some lore 2", "or two 3"));

        testConfig.set("servers", list);

        LinkedList<VCServerConfigItem> newList = (LinkedList<VCServerConfigItem>) testConfig.get("servers");

        for (int i = 0; i < newList.size(); i++) {
            Assert.assertEquals(list.get(i), newList.get(i));
        }

        System.out.println(testConfig.saveToString());
    }
}
