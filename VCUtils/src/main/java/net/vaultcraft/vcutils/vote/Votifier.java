/*
 * Copyright (C) 2012 Vex Software LLC
 * This file is part of Votifier.
 * 
 * Votifier is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Votifier is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Votifier.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.vaultcraft.vcutils.vote;

import java.io.*;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.logging.Logger;
import net.vaultcraft.vcutils.vote.crypto.RSAIO;
import net.vaultcraft.vcutils.vote.crypto.RSAKeygen;
import net.vaultcraft.vcutils.vote.model.ListenerLoader;
import net.vaultcraft.vcutils.vote.model.VoteListener;
import net.vaultcraft.vcutils.vote.net.VoteReceiver;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * The main Votifier plugin class.
 *
 * @author Blake Beaupain
 */
public class Votifier {

    /** The Votifier instance. */
    private static Votifier instance;

    /** The vote listeners. */
    private final List<VoteListener> listeners = new ArrayList<VoteListener>();

    /** The vote receiver. */
    private VoteReceiver voteReceiver;

    /** The RSA key pair. */
    private KeyPair keyPair;

    /** Debug mode flag */
    private boolean debug;

    public void onEnable() {
        Votifier.instance = this;

        File rsaDirectory = new File(VCUtils.getInstance().getDataFolder() + "/rsa");
        // Replace to remove a bug with Windows paths - SmilingDevil
        String listenerDirectory = VCUtils.getInstance().getDataFolder().toString()
                .replace("\\", "/") + "/listeners";

		/*
		 * Use IP address from server.properties as a default for
		 * configurations. Do not use InetAddress.getLocalHost() as it most
		 * likely will return the main server address instead of the address
		 * assigned to the server.
		 */
        String hostAddr = Bukkit.getServer().getIp();
        if (hostAddr == null || hostAddr.length() == 0)
            hostAddr = "0.0.0.0";

		/*
		 * Create configuration file if it does not exists; otherwise, load it
		 */
        FileConfiguration config = VCUtils.getInstance().getConfig();
        if (!(config.contains("votes.host"))) {
            try {
                config.set("votes.host", hostAddr);
                config.set("votes.port", 8192);
                config.set("votes.debug", false);

                config.set("votes.listener_folder", listenerDirectory);
                VCUtils.getInstance().saveConfig();
            } catch (Exception ex) {
                Logger.log(VCUtils.getInstance(), "Error creating configuration file");
                ex.printStackTrace();
                gracefulExit();
                return;
            }
        }

		/*
		 * Create RSA directory and keys if it does not exist; otherwise, read
		 * keys.
		 */
        try {
            if (!rsaDirectory.exists()) {
                rsaDirectory.mkdir();
                new File(listenerDirectory).mkdir();
                keyPair = RSAKeygen.generate(2048);
                RSAIO.save(rsaDirectory, keyPair);
            } else {
                keyPair = RSAIO.load(rsaDirectory);
            }
        } catch (Exception ex) {
            gracefulExit();
            return;
        }

        // Load the vote listeners.
        listenerDirectory = config.getString("votes.listener_folder");
        listeners.addAll(ListenerLoader.load(listenerDirectory));

        // Initialize the receiver.
        String host = config.getString("votes.host", hostAddr);
        int port = config.getInt("votes.port", 8192);
        debug = config.getBoolean("votes.debug", false);
        if (debug)
            Logger.log(VCUtils.getInstance(), "DEBUG mode enabled!");

        try {
            voteReceiver = new VoteReceiver(this, host, port);
            voteReceiver.start();

            Logger.log(VCUtils.getInstance(), "Voting enabled!");
        } catch (Exception ex) {
            gracefulExit();
            return;
        }
    }

    public void onDisable() {
        // Interrupt the vote receiver.
        if (voteReceiver != null) {
            voteReceiver.shutdown();
        }
        Logger.log(VCUtils.getInstance(), "Votifier disabled.");
    }

    private void gracefulExit() {
        Logger.warning(VCUtils.getInstance(), "Could not load voting properly!");
    }

    /**
     * Gets the instance.
     *
     * @return The instance
     */
    public static Votifier getInstance() {
        return instance;
    }

    /**
     * Gets the listeners.
     *
     * @return The listeners
     */
    public List<VoteListener> getListeners() {
        return listeners;
    }

    /**
     * Gets the vote receiver.
     *
     * @return The vote receiver
     */
    public VoteReceiver getVoteReceiver() {
        return voteReceiver;
    }

    /**
     * Gets the keyPair.
     *
     * @return The keyPair
     */
    public KeyPair getKeyPair() {
        return keyPair;
    }

    public boolean isDebug() {
        return debug;
    }

    public String getVersion() {
        return "1.9";
    }
}