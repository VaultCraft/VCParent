package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcessentials.VCEssentials;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.database.sql.MySQL;
import net.vaultcraft.vcutils.database.sql.Statements;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Nick on 7/21/2014.
 */
public class VCKick extends ICommand {


    public VCKick(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Format: /kick <player> (reason)");
            return;
        }

        if (args.length == 1) {
            Player player1 = Bukkit.getPlayer(args[0]);
            if (player1 == null) {
                Form.at(player, Prefix.ERROR, "No such player");
                return;
            }
            player1.kickPlayer("You have been kicked!");
            VCEssentials.getInstance().getMySQL().updateThread.add(Statements.INSERT.getSql("Kicks",
                    "'" + player1.getUniqueId().toString() + "', '" +
                            player1.getName() + "', '" +
                            player.getUniqueId().toString() + "', '" +
                            player.getName() + "', '', '" +
                            MySQL.getDate() + "'"
            ));
            return;
        }

        if (args.length > 1) {
            Player player1 = Bukkit.getPlayer(args[0]);
            if (player1 == null) {
                Form.at(player, Prefix.ERROR, "No such player");
                return;
            }

            StringBuilder reason = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                if (args.length - 1 == i)
                    reason.append(args[i]);
                else
                    reason.append(args[i]).append(" ");
            }
            player1.kickPlayer("You have been kicked for: " + reason.toString());
            VCEssentials.getInstance().getMySQL().updateThread.add(Statements.INSERT.getSql("Kicks",
                    "'" + player1.getUniqueId().toString() + "', '" +
                            player1.getName() + "', '" +
                            player.getUniqueId().toString() + "', '" +
                            player.getName() + "', '" +
                            Statements.makeSqlSafe(reason.toString()) + "', '" +
                            MySQL.getDate() + "'"
            ));
            Form.at(player, Prefix.SUCCESS, "Player: " + player1.getName() + " has been kicked.");
        }
    }
}
