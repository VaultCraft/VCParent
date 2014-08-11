package net.vaultcraft.vcutils.database.sql;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tacticalsk8er
 */
public class MySQL {

    public volatile List<String> updateThread = new ArrayList<>();
    private volatile ConcurrentHashMap<String, Long> queryThread = new ConcurrentHashMap<>();
    private volatile ConcurrentHashMap<Long, ISqlCallback> callbacks = new ConcurrentHashMap<>();
    private long queryID = 0;

    protected Plugin plugin;
    protected Connection connection = null;
    protected String url;
    private String database_username;
    private String database_password;
    protected int queries = 0;
    protected boolean enabled = true;

    private UpdateThread updateTask = new UpdateThread();
    private QueryThread queryTask = new QueryThread();

    /**
     * Provides as a connection to a MySQL database, and gives access to easy methods to modify that database.
     *
     * @param host              The URL or IP number to the MySQL database.
     * @param port              The port number of the MySQL database.
     * @param database_name     The name of the database you want to modify.
     * @param database_username A username that has access to the MySQL server and database.
     * @param database_password The password tied with the username.
     */
    public MySQL(final Plugin plugin, String host, int port, String database_name, String database_username, String database_password) {
        this.plugin = plugin;
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database_name;
        this.database_username = database_username;
        this.database_password = database_password;
        updateTask.start();
        queryTask.start();
    }

    /**
     * Default constructor for SQLite
     */
    public MySQL() {
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
            Logger.error(plugin, e);
            enabled = false;
        }
        queries++;
        return connection;
    }

    /**
     * @param sql      SQL statement.
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
        updateTask.interrupt();
        queryTask.interrupt();
        updateThread.clear();
        queryThread.clear();
        callbacks.clear();
    }

    public static String getDate() {
        java.util.Date date = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public interface ISqlCallback {
        public void onSuccess(ResultSet rs);

        public void onFailure(SQLException e);
    }

    private class QueryThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ignored) {
                }
                if (queryThread.size() > 0) {
                    if (!enabled) {
                        queryThread.clear();
                        continue;
                    }
                    for (Map.Entry entry : new ConcurrentHashMap<>(queryThread).entrySet()) {
                        String sql = (String) entry.getKey();
                        long id = (long) entry.getValue();
                        try {
                            PreparedStatement ps = getConnection().prepareStatement(sql);
                            ResultSet rs = ps.executeQuery();
                            callbacks.get(id).onSuccess(rs);
                        } catch (MySQLSyntaxErrorException e) {
                            System.out.println("SQL Statement: " + sql);
                            e.printStackTrace();
                            break;
                        } catch (SQLException e) {
                            callbacks.get(id).onFailure(e);
                        } finally {
                            queryThread.remove(sql);
                            callbacks.remove(id);
                        }
                    }
                }
            }
        }
    }

    private class UpdateThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ignored) {
                }
                if (updateThread.size() > 0) {
                    if (!enabled) {
                        queryThread.clear();
                        continue;
                    }
                    for (String s : new ArrayList<>(updateThread)) {
                        try {
                            PreparedStatement ps = getConnection().prepareStatement(s);
                            ps.executeUpdate();
                        } catch (MySQLSyntaxErrorException e) {
                            System.out.println("SQL Statement: " + s);
                            e.printStackTrace();
                            break;
                        } catch (SQLException e) {
                            Logger.error(plugin, e);
                        } finally {
                            updateThread.remove(s);
                        }
                    }
                }
            }
        }
    }
}