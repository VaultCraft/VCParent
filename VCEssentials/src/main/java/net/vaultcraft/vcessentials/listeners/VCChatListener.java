package net.vaultcraft.vcessentials.listeners;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.DecimalFormat;
import java.util.HashMap;

public class VCChatListener implements Listener {

    public int delayTime = 2;

    private HashMap<Player, Long> playerTimes = new HashMap<>();
    private static DecimalFormat df = new DecimalFormat("#,##0.#");
    private static VCChatListener instance = null;

    public VCChatListener() {
        instance = this;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if(User.fromPlayer(event.getPlayer()).getGroup().hasPermission(Group.HELPER)) {
            return;
        }

        // Do anti-advertising stuff here
        if(event.getMessage().replace(" ", "").replaceAll("[^0-9\\^.]", "").matches("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]).){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")) {
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


    public static VCChatListener getInstance() {
        return instance;
    }
}