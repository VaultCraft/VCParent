package net.vaultcraft.vcessentials.commands;

import com.mongodb.DBObject;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.UUIDFetcher;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

/**
 * Created by Sean on 10/18/2014.
 * Dedicated to UnderTheSee <3
 */
public class VCUnban extends ICommand {


    public VCUnban(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(final Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Format: /unban <player name>");
        } else if(args.length == 1) {
            UUID uuid;
            try {
                uuid = UUIDFetcher.getUUIDOf(args[0]);
            } catch (Exception e) {
                e.printStackTrace();
                Form.at(player, Prefix.ERROR, "Unable to fetch the UUID for " + args[0]);
                return;
            }
            DBObject dbObject = User.getDBObject(uuid);

            if(dbObject == null) {
                Form.at(player, Prefix.ERROR, args[0] + " does not exist in the database.");
                return;
            }
            boolean banned = (boolean) dbObject.get("Banned");
            if(banned) {
                dbObject.put("Banned", false);
                dbObject.put("TempBan", false);
                Form.at(player, Prefix.SUCCESS, args[0] + " has been unbanned!");
                DBObject dbObject1 = User.getDBObject(uuid);
                VCUtils.getInstance().getMongoDB().update(VCUtils.mongoDBName, "Users", dbObject1, dbObject);
            } else {
                Form.at(player, Prefix.ERROR, args[0] + " is not banned!");
            }
        }
    }
}
