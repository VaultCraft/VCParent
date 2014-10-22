package net.vaultcraft.vcessentials.listeners;

import net.vaultcraft.vcessentials.VCEssentials;
import net.vaultcraft.vcutils.protection.ProtectedArea;
import net.vaultcraft.vcutils.protection.ProtectionManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class VCPortalListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo().getBlock().getType().equals(Material.PORTAL)) {

            //Check portal and do warp
            for (String key : ProtectionManager.getInstance().getRegions().keySet()) {
                if (!(key.startsWith("port")))
                    continue;

                ProtectedArea area = ProtectionManager.getInstance().getRegions().get(key);
                if (area.getArea().isInArea(event.getTo())) {
                    String region = key.substring(key.indexOf("_")+1);

                    VCEssentials.getInstance().sendPlayerToServer(event.getPlayer(), region);
                }
            }
        }
    }
}
