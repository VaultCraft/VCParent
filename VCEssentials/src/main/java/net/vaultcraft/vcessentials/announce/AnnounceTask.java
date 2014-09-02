package net.vaultcraft.vcessentials.announce;

import net.vaultcraft.vcutils.chat.fancy.*;
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

    public void run() {
        JSONChatMessage curr = buildMessage("(DARK-PURPLE+BOLD)Vault_(GRAY+BOLD)Craft_(WHITE):^_(GRAY)Welcome^_(DARK-PURPLE)"+player.getName()+"{HOVER*SHOW-TEXT=You can hover on things!}_(GRAY)!");
        curr.sendToPlayer(player);
    }

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
