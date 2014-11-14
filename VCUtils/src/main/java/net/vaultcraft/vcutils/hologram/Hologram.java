package net.vaultcraft.vcutils.hologram;

import net.minecraft.server.v1_7_R4.*;
import net.vaultcraft.vcutils.VCUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Connor Hollasch
 * @since 11/12/2014
 */
public class Hologram {

    private static final double distance = 0.23;
    private List<String> lines = new ArrayList<String>();
    private List<Integer> ids = new ArrayList<Integer>();
    private boolean showing = false;
    private Location location;

    public Hologram(String... lines) {
        this.lines.addAll(Arrays.asList(lines));
    }

    public void change(String... lines) {
        destroy();
        this.lines = Arrays.asList(lines);
        show(this.location);
    }

    public void show(Location loc) {
        show(loc, Bukkit.getOnlinePlayers().toArray(new Player[0]));
    }

    public void show(Location loc, Player[] players) {
        if (showing == true) {
            try {
                throw new Exception("Is already showing!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Location first = loc.clone().add(0, (this.lines.size() / 2) * distance, 0);
        for (int i = 0; i < this.lines.size(); i++) {
            ids.addAll(showLine(first.clone(), this.lines.get(i), players));
            first.subtract(0, distance, 0);
        }
        showing = true;
        this.location = loc;
    }

    public void destroy() {
        if (showing == false) {
            try {
                throw new Exception("Isn't showing!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int[] ints = new int[this.ids.size()];
        for (int j = 0; j < ints.length; j++) {
            ints[j] = ids.get(j);
        }
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(ints);
        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
        showing = false;
        this.location = null;
    }

    private static List<Integer> showLine(Location loc, String text, Player[] players) {
        WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
        EntityWitherSkull skull = new EntityWitherSkull(world);
        skull.setLocation(loc.getX(), loc.getY() + 1 + 55, loc.getZ(), 0, 0);
        PacketPlayOutSpawnEntity skull_packet = new PacketPlayOutSpawnEntity(skull, 0);

        EntityHorse horse = new EntityHorse(world);
        horse.setLocation(loc.getX(), loc.getY() + 55, loc.getZ(), 0, 0);
        horse.setAge(-1700000);
        horse.setCustomName(text);
        horse.setCustomNameVisible(true);
        PacketPlayOutSpawnEntityLiving packedt = new PacketPlayOutSpawnEntityLiving(horse);
        for (Player player : players) {
            EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
            nmsPlayer.playerConnection.sendPacket(packedt);
            nmsPlayer.playerConnection.sendPacket(skull_packet);

            PacketPlayOutAttachEntity pa = new PacketPlayOutAttachEntity(0, horse, skull);
            nmsPlayer.playerConnection.sendPacket(pa);
        }
        return Arrays.asList(skull.getId(), horse.getId());
    }

}