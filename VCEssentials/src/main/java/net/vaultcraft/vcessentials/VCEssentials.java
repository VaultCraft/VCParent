package net.vaultcraft.vcessentials;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.vaultcraft.vcessentials.announce.AnnounceManager;
import net.vaultcraft.vcessentials.blocks.BEnderChest;
import net.vaultcraft.vcessentials.commands.*;
import net.vaultcraft.vcessentials.file.ProtectionFile;
import net.vaultcraft.vcessentials.listeners.VCChatListener;
import net.vaultcraft.vcessentials.listeners.VCHatBugfixListener;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.CommandManager;
import net.vaultcraft.vcutils.database.sql.MySQL;
import net.vaultcraft.vcutils.database.sqlite.SQLite;
import net.vaultcraft.vcutils.logging.Logger;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class VCEssentials extends JavaPlugin implements Listener {

    private static VCEssentials instance;
    private MySQL mySQL;
    private SQLite sqlite;

    public void onEnable() {
        instance = this;

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getPluginManager().registerEvents(this, this);

        ProtectionFile.getInstance().load();
        initCommands();
        mySQL = VCUtils.getInstance().getMySQL();
        sqlite = VCUtils.getInstance().getSqlite();
        BEnderChest enderChest = new BEnderChest("vault", Group.WOLF, "enderchest");
        CommandManager.addCommand(enderChest);
        Bukkit.getPluginManager().registerEvents(enderChest, this);
        Bukkit.getPluginManager().registerEvents(new VCChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new VCHatBugfixListener(), this);

        saveDefaultConfig();
    }

    public static VCEssentials getInstance() {
        return instance;
    }

    private void initCommands() {
        CommandManager.addCommand(new VCPromote("promote", Group.ADMIN, "demote", "setgroup"));
        CommandManager.addCommand(new VCMessage("message", Group.COMMON, "msg", "tell", "whisper"));
        CommandManager.addCommand(new VCReply("reply", Group.COMMON, "r", "respond"));
        CommandManager.addCommand(new VCTeleport("tp", Group.MOD, "teleport"));
        CommandManager.addCommand(new VCGamemode("gm", Group.ADMIN, "gamemode"));
        CommandManager.addCommand(new VCFly("fly", Group.MOD));
        CommandManager.addCommand(new VCGive("give", Group.ADMIN, "g"));
        CommandManager.addCommand(new VCTime("time", Group.MOD));
        CommandManager.addCommand(new VCWeather("weather", Group.MOD));
        CommandManager.addCommand(new VCHeal("heal", Group.ADMIN, "h"));
        CommandManager.addCommand(new VCFeed("feed", Group.ADMIN));
        CommandManager.addCommand(new VCGod(this, "god", Group.ADMIN, "godmode"));
        CommandManager.addCommand(new VCKick("kick", Group.HELPER));
        CommandManager.addCommand(new VCMute(this, "mute", Group.HELPER));
        CommandManager.addCommand(new VCBan(this, "ban", Group.MOD));
        CommandManager.addCommand(new VCUnban(this, "unban", Group.MOD));
        CommandManager.addCommand(new VCMoney("money", Group.COMMON, "bal", "balance"));
        CommandManager.addCommand(new VCToken("tokens", Group.COMMON));
        CommandManager.addCommand(new VCTeleportHere("tphere", Group.MOD, "teleporthere"));
        CommandManager.addCommand(new VCSpeed("speed", Group.ADMIN));
        CommandManager.addCommand(new VCButcher("butcher", Group.ADMIN, "killall"));
        CommandManager.addCommand(new VCAddSign("addsign", Group.DEVELOPER, "createsign", "linksign"));
        CommandManager.addCommand(new VCRemoveSign("removesign", Group.DEVELOPER, "deletesign", "unlinksign"));
        CommandManager.addCommand(new VCWorld("world", Group.ADMIN));
        CommandManager.addCommand(new VCServer("server", Group.COMMON));
        CommandManager.addCommand(new VCHub("hub", Group.COMMON, "lobby", "cloud"));
        CommandManager.addCommand(new VCAnnounceToggle("announce", Group.COMMON, "announcements"));
        CommandManager.addCommand(new VCHat("hat", Group.SLIME));
        CommandManager.addCommand(new VCClearChat("ccg", Group.MOD, "clearchatglobal"));
        CommandManager.addCommand(new VCClearChatPersonal("ccp", Group.COMMON, "clearchat"));
        CommandManager.addCommand(new VCChatDelay("chatdelay", Group.ADMIN, "ccd"));
        CommandManager.addCommand(new VCVote("vote", Group.COMMON, "v"));

        //protection
        CommandManager.addCommand(new VCProtection("protect", Group.DEVELOPER, "p", "region", "prot", "protection"));

        //redirects
        CommandManager.addRedirect("gms", "gamemode 0");
        CommandManager.addRedirect("gmc", "gamemode 1");
        CommandManager.addRedirect("gma", "gamemode 2");
        CommandManager.addRedirect("pay", "money pay");

        //whitelist
        CommandManager.addPluginWhitelist("/");
        CommandManager.addPluginWhitelist("ec");
        CommandManager.addPluginWhitelist("reload");
        CommandManager.addPluginWhitelist("rl");
        CommandManager.addPluginWhitelist("npc");
        CommandManager.addPluginWhitelist("buy");
        CommandManager.addPluginWhitelist("buycraft");

        //LOL
        CommandManager.addCommand(new VCSpamFunStuff("spam", Group.OWNER, "funny", "bots"));
    }

    public void onDisable() {
        ProtectionFile.getInstance().save();

        saveDefaultConfig();
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public SQLite getSqlite() { return sqlite; }

    public void sendPlayerToServer(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);
        player.sendPluginMessage(VCEssentials.getInstance(), "BungeeCord", out.toByteArray());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //console commands
        switch (cmd.getName().toLowerCase()) {
            case "promote": {
                if (args.length < 2) {
                    Logger.log(this, "Format: /setgroup <user> <rank>");
                    return true;
                }

                Player wrapped = Bukkit.getPlayer(args[0]);
                if (wrapped == null) {
                    Logger.log(this, "No such player! Format: /setgroup <user> <rank>");
                    return true;
                }

                Group select = Group.fromString(args[1]);
                if (select == null) {
                    Logger.log(this, "No such group! Format: /setgroup <user> <rank>");
                    return true;
                }

                String message = wrapped.getName() + " %s " + select.getName();

                if (args.length >= 3 && args[2].equalsIgnoreCase("-a")) {
                    //add
                    User.fromPlayer(wrapped).getGroup().merge(select);
                    message = message.replace("%s", "added to group");
                } else if (args.length >= 3 && args[2].equalsIgnoreCase("-r")) {
                    //remove
                    User.fromPlayer(wrapped).getGroup().remove(select);
                    message = message.replace("%s", "removed from group");
                } else {
                    //set
                    User.fromPlayer(wrapped).setGroup(select);
                    message = message.replace("%s", "set group to");
                }

                Logger.log(this, message);
                break;
            }
            case "redir": {
                if (!(sender instanceof Player))
                    return true;

                String send = "";
                for (String arg : args) {
                    send+=arg+" ";
                }
                System.out.println("Redirecting command to chat="+send);
                ((Player)sender).chat("/"+send);
                return true;
            }
            case "unban": {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.YELLOW + "Please specify a player to unban!");
                } else if(args.length == 1) {
                    final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (offlinePlayer == null) {
                        sender.sendMessage(ChatColor.RED+"No such player!");
                        return true;
                    }
                    final User theUser = new User(offlinePlayer.getPlayer());
                    Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                        public void run() {
                            theUser.setBanned(false, null);
                            User.remove(offlinePlayer.getPlayer());
                            sender.sendMessage(ChatColor.YELLOW+"You unbanned " + offlinePlayer.getName() + " from the server!");
                        }
                    }, 10);
                }
            }
            case "announce": {
                if (!(sender.isOp())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lERROR&7: &fYou do not have permission to use this command!"));
                    return true;
                }

                if (args.length == 0) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lERROR&7: &fPlease specify a message to broadcast!"));
                }

                String msg = "";
                for (String arg : args) {
                    msg+=(arg+" ");
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                }
            }
        }
        return true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        AnnounceManager.subscribeTask(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        AnnounceManager.unsubscribeTask(event.getPlayer());
    }
}
