package net.vaultcraft.vcutils.database.sqlite;

import net.vaultcraft.vcutils.database.sql.MySQL;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * (This class is untested but should work!)
 *
 * @author Sean "CyberKitsune" McClenaghan
 */
public class SQLite extends MySQL {

    /**
     * Provides as a connection to a SQLite database, and gives access to easy methods to modify that database.
     *
     * @param plugin    The plugin using this class.
     * @param database  The path to the database file.
     */
    public SQLite(final Plugin plugin, String database) {
        this.url = "jdbc:sqlite:" + database;
        this.plugin = plugin;
        updateTask.start();
        queryTask.start();

    }

    @Override
    public Connection getConnection() {
        try {
            if (queries >= 1000) {
                if (connection != null) {
                    connection.close();
                }

                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(url);
                queries = 0;
            }
            if (connection == null || connection.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(url);
            }
        } catch (SQLException | ClassNotFoundException e) {
            Logger.error(plugin, e);
            enabled = false;
        }
        queries++;
        return connection;
    }

    @Override
    public void close() throws SQLException {
        connection.commit();
        super.close();
    }
}
