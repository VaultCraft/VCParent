package net.vaultcraft.vcessentials.commands;

import com.mongodb.DBObject;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.UUIDFetcher;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

/**
 * Created by Sean on 10/18/2014.
 */
public class VCUnban extends ICommand {

    private Plugin plugin;

    public VCUnban(Plugin plugin, String name, Group permission, String... aliases) {
        super(name, permission, aliases);
        this.plugin = plugin;
    }

    @Override
    public void processCommand(final Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Format: /unban <player name>");
        } else if(args.length == 1) {
            UUID uuid;
            try {
                uuid = UUIDFetcher.getUUIDOf(args[1]);
            } catch (Exception e) {
                e.printStackTrace();
                Form.at(player, Prefix.ERROR, "Unable to fetch the UUID for " + args[1]);
                return;
            }
            DBObject dbObject = VCUtils.getInstance().getMongoDB().query("VaultCraft", "Users", "UUID", uuid);

            if(dbObject == null) {
                Form.at(player, Prefix.ERROR, args[1] + " does not exist in the database.");
                return;
            }
            boolean banned = (boolean) dbObject.get("Banned");
            if(banned) {
                dbObject.put("Banned", false);
                Form.at(player, Prefix.SUCCESS, args[1] + " has been unbanned!");
                DBObject dbObject1 = VCUtils.getInstance().getMongoDB().query("VaultCraft", "Users", "UUID", uuid);
                VCUtils.getInstance().getMongoDB().update("VaultCraft", "Users", dbObject1, dbObject);
            } else {
                Form.at(player, Prefix.ERROR, args[1] + " is not banned!");
            }
        }
    }
}
