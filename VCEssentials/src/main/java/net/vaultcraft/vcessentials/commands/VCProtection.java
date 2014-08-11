package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcessentials.file.ProtectionFile;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.hook.WorldEditHook;
import net.vaultcraft.vcutils.protection.Area;
import net.vaultcraft.vcutils.protection.ProtectedArea;
import net.vaultcraft.vcutils.protection.ProtectionManager;
import net.vaultcraft.vcutils.protection.flag.FlagType;
import net.vaultcraft.vcutils.string.StringUtils;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.json.simple.parser.ParseException;

import java.util.HashMap;

/**
 * Created by Connor on 7/21/14. Designed for the VCUtils project.
 */

public class VCProtection extends ICommand {

    public VCProtection(String name, Group permission, String... aliases) {
        super(name, permission, aliases);

        //init subcommands for help
        subCmds.put("add <name>", "Create a region with current selection.");
        subCmds.put("remove <name>", "Remove the given region.");
        subCmds.put("flag <name> <flag> <value>", "Flag the given region.");
        subCmds.put("setpriority <name> <priority>", "Set the regions priority.");
        subCmds.put("check", "Gather region info for current region.");
    }

    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            //DO HELP
            Form.atHelp(player, this);
            return;
        }

        String[] fixedArgs = StringUtils.buildFromArray(args, 1).split(" ");

        switch (args[0].toLowerCase()) {
            case "help": {
                Form.atHelp(player, this);
                break;
            }
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
            case "scan":
            case "current":
            case "info":
            case "check": {
                checkRegion(player, fixedArgs);
                break;
            }
            case "setpriority":
            case "priority":
            case "changepriority": {
                changeRegionPriority(player, fixedArgs);
                break;

            }
            case "list": {
                listRegions(player, fixedArgs);
                break;
            }
            default: {
                Form.at(player, Prefix.ERROR, "No such sub command! Type /protect for help");
                return;
            }
        }
    }

    private void changeRegionPriority(Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Please specify a region name!");
            return;
        }

        ProtectedArea area = ProtectionManager.getInstance().getArea(args[0]);
        if (area == null) {
            Form.at(player, Prefix.ERROR, "No such region as: &e"+args[0]+Prefix.ERROR.getChatColor()+"!");
            return;
        }

        if (args.length == 1) {
            Form.at(player, Prefix.ERROR, "Please specify a priority set to!");
            return;
        }

        int priority = 0;
        try {
            priority = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            Form.at(player, Prefix.ERROR, "&e"+args[1]+Prefix.ERROR.getChatColor()+" is not a valid number!");
            return;
        }

        area.setPriority(priority);
        Form.at(player, Prefix.SUCCESS, "You changed &e"+args[0]+"'s"+Prefix.SUCCESS.getChatColor()+" priority to &e&n"+priority+"&r"+Prefix.SUCCESS.getChatColor()+"!");
        saveAll();
    }

    private void addRegion(Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Please specify a name!");
            return;
        }

        if (ProtectionManager.getInstance().getArea(args[0]) != null) {
            Form.at(player, Prefix.ERROR, "A region with this name already exists!");
            return;
        }

        Area create = WorldEditHook.getInstance().getFromSelection(player);
        if (create == null) {
            Form.at(player, Prefix.ERROR, "You must have a valid selection to continue!");
            return;
        }

        ProtectedArea pa = new ProtectedArea(create);
        ProtectionManager.getInstance().addToProtection(args[0], pa);
        Form.at(player, Prefix.SUCCESS, "Region: &e" + args[0] + Prefix.SUCCESS.getChatColor() + " created!");
        saveAll();
    }

    private void flagRegion(Player player, String[] args) {
        if (args.length == 0 || args[0].equals("")) {
            Form.at(player, Prefix.ERROR, "Please specify a region name!");
            return;
        }

        ProtectedArea area = ProtectionManager.getInstance().getArea(args[0].toLowerCase());
        if (area == null) {
            Form.at(player, Prefix.ERROR, "No such region as: &e"+args[0]+Prefix.ERROR.getChatColor()+"!");
            return;
        }

        if (args.length == 1) {
            Form.at(player, Prefix.ERROR, "Please specify a flag!");
            return;
        }

        FlagType t = FlagType.fromString(args[1]);
        if (t == null) {
            Form.at(player, Prefix.ERROR, "No such flag! Here are a list of valid flags...");
            String all = "&c";
            for (FlagType x : FlagType.values()) {
                all+=(x.getAliases()[0]+", ");
            }
            all = all.substring(0, all.length()-2);
            Form.at(player, Prefix.NOTHING, all);
            return;
        }

        boolean cancel = !(args.length >= 3 ? parseBoolean(args[2]) : true);
        area.addToProtection(t, cancel);

        Form.at(player, Prefix.SUCCESS, "Area: &e"+args[0]+Prefix.SUCCESS.getChatColor()+" will now &e&o"+(cancel ? "cancel" : "not cancel")+Prefix.SUCCESS.getChatColor()+" a(n) &e"+t.getAliases()[0]);
        saveAll();
    }

    private static boolean parseBoolean(String input) {
        switch (input.toLowerCase()) {
            case "true":
            case "yes":
            case "allow":
            case "notcancel":
                return true;
            case "false":
            case "no":
            case "deny":
            case "cancel":
                return false;
        }
        return false;
    }

    private void removeRegion(Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Please specify a name!");
            return;
        }

        if (ProtectionManager.getInstance().getArea(args[0]) == null) {
            Form.at(player, Prefix.ERROR, "A region with this name does not exist!");
            return;
        }

        ProtectionManager.getInstance().removeFromProtection(args[0]);
        Form.at(player, Prefix.SUCCESS, "Region: &e" + args[0] + Prefix.SUCCESS.getChatColor() + " removed!");
        saveAll();
    }

    private void saveAll() {
        ProtectionFile.getInstance().save();
    }

    private void checkRegion(Player player, String[] args) {
        if (args.length != 0 && !(args[0].equals(""))) {
            ProtectedArea find = ProtectionManager.getInstance().getArea(args[0]);
            if (find == null) {
                Form.at(player, Prefix.ERROR, "No such region!");
                return;
            }

            info(player, args[0], find);
            return;
        }
        HashMap<String, ProtectedArea> areas = ProtectionManager.getInstance().getRegions();
        for (String name : areas.keySet()) {
            ProtectedArea area = areas.get(name);
            if (area.getArea().isInArea(player.getLocation())) {
                info(player, name, area);
            }
        }
    }

    private void info(Player player, String name, ProtectedArea area) {
        Location min = area.getArea().getMin();
        Location max = area.getArea().getMax();

        String minStr = (min == null ? "MIN" : min.getBlockX()+", "+min.getBlockY()+", "+min.getBlockZ());
        String maxStr = (max == null ? "MAX" : max.getBlockX()+", "+max.getBlockY()+", "+max.getBlockZ());

        Form.at(player, "Information on region - "+name);
        String say = "&eFlags: &c(";
        for (FlagType t : area.getProtection().keySet()) {
            say+=(t.getAliases()[0]+" cancel? "+(area.getProtection().get(t) ? "yes" : "no")+", ");
        }
        say = (say.contains(", ") ? say.substring(0, say.length()-2) : say);
        say+=")";
        Form.at(player, Prefix.NOTHING, say);
        Form.at(player, Prefix.NOTHING, "  &cMin: &e&n("+minStr+")&r&c, Max: &e&n("+maxStr+")");
    }

    private void listRegions(Player player, String[] args) {
        int i = 0;
        for (String name : ProtectionManager.getInstance().getRegions().keySet()) {
            Form.at(player, Prefix.NOTHING, "&cRegion "+(++i)+": &e"+name);
        }
    }
}
