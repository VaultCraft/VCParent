package net.vaultcraft.vcutils.title;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TextConverter
{
    public static String convert(String text)
    {
        if ((text == null) || (text.length() == 0)) {
            return "\"\"";
        }
        int len = text.length();
        StringBuilder sb = new StringBuilder(len + 4);


        sb.append('"');
        for (int i = 0; i < len; i++)
        {
            char c = text.charAt(i);
            switch (c)
            {
                case '"':
                case '\\':
                    sb.append('\\');
                    sb.append(c);
                    break;
                case '/':
                    sb.append('\\');
                    sb.append(c);
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                default:
                    if (c < ' ')
                    {
                        String t = "000" + Integer.toHexString(c);
                        sb.append("\\u" + t.substring(t.length() - 4));
                    }
                    else
                    {
                        sb.append(c);
                    }
                    break;
            }
        }
        sb.append('"');
        return sb.toString();
    }

    public static String setPlayerName(Player player, String text)
    {
        text = replaceVariable(text, "PLAYER", player.getName());
        text = replaceVariable(text, "DISPLAYNAME", player.getDisplayName());
        text = replaceVariable(text, "STRIPPEDDISPLAYNAME", ChatColor.stripColor(player.getDisplayName()));
        return text;
    }

    static String replaceVariable(String str0, String variable, String str1)
    {
        try
        {
            if (str0.toLowerCase().contains("{" + variable.toLowerCase() + "}")) {
                return str0.replaceAll("(?i)\\{" + variable + "\\}", str1);
            }
            return str0;
        }
        catch (Exception e) {}
        return str0;
    }
}
