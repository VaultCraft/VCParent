package net.vaultcraft.vcessentials.announce;

import net.vaultcraft.vcutils.chat.fancy.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by Connor on 9/1/14. Designed for the VCUtils project.
 */

public class AnnounceTask implements Runnable {

    private Player player;

    public AnnounceTask(Player player) {
        this.player = player;
    }

    public boolean _muted = true;
    private int index = 0;

    public void run() {
        if (_muted)
            return;

        if (++index >= messages.length)
            index = 0;

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5&m-----------------------------------------------------"));
        header.sendToPlayer(player);
        player.sendMessage("");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &5- &7"+messages[index].replace("%player%", player.getName())));
        player.sendMessage("");
        disable.sendToPlayer(player);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5&m-----------------------------------------------------"));
    }

    private static JSONChatMessage header = buildMessage("(DARK-PURPLE+BOLD)Vault{CLICK*OPEN-URL=http://vaultcraft.net}_(GRAY+BOLD)Craft{CLICK*OPEN-URL=http://vaultcraft.net}");
    private static JSONChatMessage disable = buildMessage("(DARK-GRAY+ITALIC)Click to disable messages{CLICK*RUN-COMMAND=/announce}");
    private static String[] messages = {
        "Welcome &d%player% &7to our network!",
        "We are currently in &d&npre-alpha&r &7so be sure to report bugs!",
        "Looking to help out the server in return for some &dEPIC &7  perks? Click the \"VaultCraft\" text above to go to our website!"
    };

    private static JSONChatMessage buildMessage(String input) {
        String[] split = input.split("_");
        JSONChatMessage message = new JSONChatMessage("", JSONChatColor.WHITE, null);

        boolean spaceNext = false;
        for (String str : split) {
            if (str.length() == 0)
                continue;

            String colors = str.substring(1, str.indexOf(")"));
            JSONChatFormat extraClr = null;
            if (colors.contains("+")) {
                //has an extra format
                String clr = colors.substring(colors.indexOf("+")+1);
                extraClr = JSONChatFormat.valueOf(clr.toUpperCase().replace("-", "_"));
            }
            JSONChatColor color = JSONChatColor.valueOf(colors.substring(0, (colors.contains("+") ? colors.indexOf("+") : colors.length())).replace("-", "_").toUpperCase());

            str = str.substring(str.indexOf(")")+1);

            String put = str.substring(0, (str.contains("}") ? str.indexOf("{") : (str.endsWith("^") ? str.length()-1 : str.length())));
            if (spaceNext) {
                spaceNext = false;
                put = " "+put;
            }
            JSONChatExtra extra = new JSONChatExtra(put, color, Arrays.asList(extraClr));

            if (str.contains("}")) {
                //has metadata
                String type = str.substring(str.indexOf("{")+1, str.indexOf("*", str.indexOf("{")));
                String value = str.substring(str.indexOf("*")+1, str.indexOf("=")).replace("-", "_");
                String valueResult = str.substring(str.indexOf("=") + 1, str.indexOf("}"));

                if (type.toUpperCase().equals("HOVER")) {
                    extra.setHoverEvent(JSONChatHoverEventType.valueOf(value.toUpperCase()), valueResult);
                } else {
                    extra.setClickEvent(JSONChatClickEventType.valueOf(value.toUpperCase()), valueResult);
                }
                str = str.substring(0, str.indexOf("{"));
            }

            if (str.endsWith("^"))
                spaceNext = true;

            message.addExtra(extra);
        }

        return message;
    }
}
