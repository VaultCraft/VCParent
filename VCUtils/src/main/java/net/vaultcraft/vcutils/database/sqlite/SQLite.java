package net.vaultcraft.vcutils.database.sqlite;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import net.vaultcraft.vcutils.database.sql.MySQL;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.plugin.Plugin;

import java.sql.*;

/**
 * (This class is untested but should work!)
 *
 * @author Sean "CyberKitsune" McClenaghan
 */
public class SQLite {

    private final Plugin plugin;
    private Connection connection;

    /**
     * Provides as a connection to a SQLite database, and gives access to easy methods to modify that database.
     *
     * @param plugin    The plugin using this class.
     * @param database  The path to the database file.
     */
    public SQLite(final Plugin plugin, String database) {
        String url = "jdbc:sqlite:" + database;
        this.plugin = plugin;
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Run a SQLite query
     * @param sql SQL statement to run
     * @param callback Callback to use with the query
     */
    public void doQuery(String sql, MySQL.ISqlCallback callback) {
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            callback.onSuccess(rs);
        } catch (MySQLSyntaxErrorException e) {
            System.out.print("SQL Statement: " + sql);
            e.printStackTrace();
        } catch (SQLException e) {
            callback.onFailure(e);
        }
    }

    /**
     * Run a SQLite update
     * @param sql SQL to run in update mode.
     */
    public void doUpdate(String sql) {
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.error(plugin, e);
        }
    }


    public void close() throws SQLException {
        connection.close();
    }
}
