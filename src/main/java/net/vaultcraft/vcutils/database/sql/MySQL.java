package net.vaultcraft.vcutils.database.sql;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tacticalsk8er
 */
public class MySQL {

    public static volatile List<String> updateThread = new ArrayList<>();
    private static volatile ConcurrentHashMap<String, Long> queryThread = new ConcurrentHashMap<>();
    private static volatile ConcurrentHashMap<Long, ISqlCallback> callbacks = new ConcurrentHashMap<>();
    private static long queryID = 0;

    private Connection connection = null;
    private String url;
    private String database_username;
    private String database_password;
    private int queries = 0;

    private BukkitTask updateTask;
    private BukkitTask queryTask;

    /**
     * Provides as a connection to a MySQL database, and gives access to easy methods to modify that database.
     *
     * @param host              The URL or IP number to the MySQL database.
     * @param port              The port number of the MySQL database.
     * @param database_name     The name of the database you want to modify.
     * @param database_username A username that has access to the MySQL server and database.
     * @param database_password The password tied with the username.
     */
    public MySQL(Plugin plugin, String host, int port, String database_name, String database_username, String database_password) {
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database_name;
        this.database_username = database_username;
        this.database_password = database_password;

        //Update Thread
        updateTask = Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (updateThread.size() < 0) {
                        try {
                            PreparedStatement ps = getConnection().prepareStatement(updateThread.get(0));
                            updateThread.remove(0);
                            ps.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        //Query Thread
        queryTask = Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (queryThread.size() < 0) {
                        for (Map.Entry entry : new ConcurrentHashMap<>(queryThread).entrySet()) {
                            String sql = (String) entry.getKey();
                            long id = (long) entry.getValue();
                            try {
                                PreparedStatement ps = getConnection().prepareStatement(sql);
                                ResultSet rs = ps.executeQuery();
                                callbacks.get(id).onSuccess(rs);
                            } catch (SQLException e) {
                                callbacks.get(id).onFailure(e);
                            }

                            queryThread.remove(sql);
                            callbacks.remove(id);
                        }
                    }
                }
            }
        });
    }

    /**
     * @return Connection
     */
    public Connection getConnection() {
        try {
            if (queries >= 1000) {
                if (connection != null) {
                    connection.close();
                }
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(url, database_username, database_password);
                queries = 0;
            }
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(url, database_username, database_password);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        queries++;
        return connection;
    }

    /**
     *
     * @param sql SQL statement.
     * @param callback Callback class.
     */
    public void addQuery(String sql, ISqlCallback callback) {
        queryThread.put(sql, queryID);
        callbacks.put(queryID, callback);
        queryID++;
    }

    /**
     * Closes the connection to the MySQL database.
     *
     * @throws SQLException
     */
    public void close() throws SQLException {
        if (connection != null)
            connection.close();
        updateTask.cancel();
        queryTask.cancel();
    }

    public interface ISqlCallback {
        public void onSuccess(ResultSet rs);

        public void onFailure(Exception e);
    }
}