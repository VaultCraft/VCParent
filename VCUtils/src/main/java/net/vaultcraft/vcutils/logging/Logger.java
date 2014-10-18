package net.vaultcraft.vcutils.logging;

import org.bukkit.plugin.Plugin;
import org.fusesource.jansi.Ansi;

/**
 * Created by tacticalsk8er on 7/21/2014.
 */
public class Logger {

    private static final String ansi_yellow = Ansi.ansi().fg(Ansi.Color.YELLOW).boldOff().toString();
    private static final String ansi_red = Ansi.ansi().fg(Ansi.Color.RED).boldOff().toString();

    private static final String ansi_reset = Ansi.ansi().a(Ansi.Attribute.RESET).boldOff().toString();

    public static void log(Plugin plugin, String message) {
        System.out.println(ansi_yellow + "(" + plugin.getName() + ") " + ansi_reset + message + ansi_reset);
    }

    public static void error(Plugin plugin, Exception e) {
        System.out.println(ansi_red + "(ERROR) " + ansi_reset + "[" + plugin.getName() + "] " + "Error preparing statement! Stack trace below..." + ansi_reset);
        e.printStackTrace();
    }

    public static void debug(Plugin plugin, String text) {
        System.out.println(ansi_yellow + "(DE) " + ansi_reset + "[" + plugin.getName() + "] " + text + ansi_reset);
    }


    public static void warning(Plugin plugin, String message) {
        System.out.println(ansi_yellow + "(WARNING) " + ansi_reset + "[" + plugin.getName() + "] " + message + ansi_reset);
    }
}
