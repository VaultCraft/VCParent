package net.vaultcraft.vcutils.chat.fancy;

/**
 * Created by Connor on 9/1/14. Designed for the VCUtils project.
 */

import org.json.simple.JSONObject;

import java.util.List;

/**
 * User: bobacadodl
 * Date: 10/27/13
 * Time: 9:03 PM
 */
public class JSONChatExtra {
    private JSONObject chatExtra;

    public JSONChatExtra(String text, JSONChatColor color, List<JSONChatFormat> formats) {
        chatExtra = new JSONObject();
        chatExtra.put("text", text);
        chatExtra.put("color", color.getColorString());
        for (JSONChatFormat format : formats) {
            if (format == null)
                continue;

            chatExtra.put(format.getFormatString(), true);
        }
    }

    public void setClickEvent(JSONChatClickEventType action, String value) {
        JSONObject clickEvent = new JSONObject();
        clickEvent.put("action", action.getTypeString());
        clickEvent.put("value", value);
        chatExtra.put("clickEvent", clickEvent);
    }

    public void setHoverEvent(JSONChatHoverEventType action, String value) {
        JSONObject hoverEvent = new JSONObject();
        hoverEvent.put("action", action.getTypeString());
        hoverEvent.put("value", value);
        chatExtra.put("hoverEvent", hoverEvent);
    }

    public JSONObject toJSON() {
        return chatExtra;
    }
}