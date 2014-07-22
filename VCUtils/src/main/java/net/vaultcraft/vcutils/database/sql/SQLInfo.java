package net.vaultcraft.vcutils.database.sql;

import net.vaultcraft.vcutils.config.ClassConfig;

/**
 * Created by tacticalsk8er on 7/21/2014.
 */
public class SQLInfo {

    @ClassConfig.Config(path = "MySQL.Host")
    public static String host = "localhost";
    @ClassConfig.Config(path = "MySQL.Port")
    public static int port = 3306;
    @ClassConfig.Config(path = "MySQL.DatabaseName")
    public static String database_name = "VaultCraft";
    @ClassConfig.Config(path = "MySQL.Username")
    public static String username = "root";
    @ClassConfig.Config(path = "MySQL.Password")
    public static String password = "password";
}
