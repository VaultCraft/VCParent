package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcessentials.file.ProtectionFile;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.hook.WorldEditHook;
import net.vaultcraft.vcutils.protection.Area;
import net.vaultcraft.vcutils.protection.ProtectedArea;
import net.vaultcraft.vcutils.protection.ProtectionManager;
import net.vaultcraft.vcutils.string.StringUtils;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

/**
 * Created by Connor on 7/21/14. Designed for the VCUtils project.
 */

public class VCProtection extends ICommand {

    public VCProtection(String name, Group permission, String... aliases) {
        super(name, permission, aliases);

        //init subcommands for help
        subCmds.put("add <name>", "Create a region with current selection.");
        subCmds.put("remove <name>", "Remove the given region.");
        subCmds.put("flag <name> <flag> <value>", "Flag the given region");
    }

    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            //DO HELP
            Form.atHelp(player, this);
            return;
        }

        String[] fixedArgs = StringUtils.buildFromArray(args, 1).split(" ");

        switch (args[0].toLowerCase()) {
            case "create":
            case "new":
            case "a":
            case "add": {
                addRegion(player, fixedArgs);
                break;
            }
            case "f":
            case "flag": {
                flagRegion(player, fixedArgs);
                break;
            }
            case "rem":
            case "rm":
            case "delete":
            case "r":
            case "remove": {
                removeRegion(player, fixedArgs);
                break;
            }
            default: {
                Form.at(player, Prefix.ERROR, "No such sub command! Type /protect for help");
                return;
            }
        }
    }

    private void addRegion(Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Please specify a name!");
            return;
        }

        Area create = WorldEditHook.getInstance().getFromSelection(player);
        if (create == null) {
            Form.at(player, Prefix.ERROR, "You must have a valid selection to continue!");
            return;
        }

        ProtectedArea pa = new ProtectedArea(create);
        ProtectionManager.getInstance().addToProtection(args[0], pa);
        ProtectionFile.getInstance().saveAll();
        Form.at(player, Prefix.SUCCESS, "Region: &e" + args[0] + Prefix.SUCCESS.getChatColor() + " created!");
    }

    private void flagRegion(Player player, String[] args) {

    }

    private void removeRegion(Player player, String[] args) {

    }
}
