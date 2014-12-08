package net.vaultcraft.vcessentials.listeners;

import net.vaultcraft.vcessentials.commands.VCIgnore;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VCChatListener implements Listener {

    public int delayTime = 2;

    public static List<Player> afkPlayers = new ArrayList<>();
    public static List<Player> emotingPlayers = new ArrayList<>();

    private HashMap<Player, Long> playerTimes = new HashMap<>();
    private static DecimalFormat df = new DecimalFormat("#,##0.#");
    private static VCChatListener instance = null;

    public VCChatListener() {
        instance = this;
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if(User.fromPlayer(event.getPlayer()).isMuted()) {
            return;
        }

        if(emotingPlayers.contains(event.getPlayer())) {
            emotingPlayers.remove(event.getPlayer());
            event.setFormat("* " + event.getFormat().replaceFirst("\\:", ""));
        }
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(event.getPlayer() == p) {
                continue;
            }
            if(VCIgnore.isIgnored(p, event.getPlayer()) && !User.fromPlayer(event.getPlayer()).getGroup().hasPermission(Group.HELPER)) {
                event.getRecipients().remove(p);
                continue;
            }
            if(event.getMessage().toLowerCase().contains(p.getDisplayName().toLowerCase())) {
                if(afkPlayers.contains(p)) {
                    Form.at(event.getPlayer(), Prefix.WARNING, p.getName() + " is currently AFK and may not respond to your message!");
                }
                p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1, 1);
                String modifiedMessage = event.getMessage();
                modifiedMessage = modifiedMessage.replaceAll("(?i)" + p.getDisplayName(), ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + p.getDisplayName() + User.fromPlayer(event.getPlayer()).getGroup().getHighest().getMessageColor());
                p.sendMessage(String.format(event.getFormat(), event.getPlayer().getDisplayName(), modifiedMessage));
                event.getRecipients().remove(p);
            }
        }

        if (!event.getPlayer().isOp() && denySwears(event.getMessage())) {
            Form.at(event.getPlayer(), Prefix.WARNING, "Please keep swearing to a minimum!");
            event.setCancelled(true);
            return;
        }

        if(User.fromPlayer(event.getPlayer()).getGroup().hasPermission(Group.HELPER) || User.fromPlayer(event.getPlayer()).getGroup().getAllGroups().contains(Group.YOUTUBE)) {
            return;
        }

        // Do anti-advertising stuff here
        if(event.getMessage().replace(" ", "").replaceAll("[^0-9\\^.]", "").matches("^.*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]).){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]).*$")) {
            Form.at(event.getPlayer(), Prefix.WARNING, "Please do not put IP addresses in chat!");
            event.setCancelled(true);
            return;
        }

        if(event.getMessage().matches("^.*[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9]\\.[a-zA-Z]{2,}.*$")) {
            Form.at(event.getPlayer(), Prefix.WARNING, "Please do not put domain names in chat!");
            event.setCancelled(true);
            return;
        }
        // Chat delay stuff
        if (delayTime < 1) {
            return;
        }

        if(playerTimes.containsKey(event.getPlayer())) {
            if(playerTimes.get(event.getPlayer()) + (delayTime * 1000) > System.currentTimeMillis()) {
                double timeDiff = ((double)(playerTimes.get(event.getPlayer()) + ((double)delayTime * 1000.0)) - (double)System.currentTimeMillis()) / 1000.0;
                Form.at(event.getPlayer(), Prefix.ERROR, "You can't talk for another " + df.format(timeDiff) + " seconds.");
                event.setCancelled(true);
            } else {
                playerTimes.put(event.getPlayer(), System.currentTimeMillis());
            }
        } else {
            playerTimes.put(event.getPlayer(), System.currentTimeMillis());
        }
    }

    private static boolean denySwears(String input) {
        input = input.toLowerCase();
        input = input.replace("$", "s").replace("@", "a").replace("(", "c");
        input = input.replaceAll("[^a-zA-Z]", "");
        if (input.contains("fuck") || input.contains("bitch") || input.contains("cunt")
        || input.contains("nigger") || input.contains("faggot") || input.contains("fag"))
            return true;
        return false;
    }


    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if(afkPlayers.contains(event.getPlayer())) {
            afkPlayers.remove(event.getPlayer());
        }
    }


    public static VCChatListener getInstance() {
        return instance;
    }
}
