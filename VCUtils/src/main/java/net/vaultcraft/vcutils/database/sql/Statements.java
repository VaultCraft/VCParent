package net.vaultcraft.vcutils.database.sql;


import java.util.regex.Matcher;

/**
 * Created by tacticalsk8er on 7/21/2014.
 */
public enum Statements {

    TABLE("CREATE TABLE IF NOT EXISTS ?(ID INT NOT NULL AUTO_INCREMENT, PRIMARY KEY(ID), ?) DEFAULT CHARSET=utf32", 2),
    TABLE_SQLITE("CREATE TABLE IF NOT EXISTS ?(ID INTEGER NOT NULL PRIMARY KEY autoincrement, ?)", 2),
    INSERT("INSERT INTO ? VALUES(default, ?)", 2),
    UPDATE("UPDATE ? SET ? WHERE ?", 3),
    QUERYALL("SELECT * FROM ?", 1),
    QUERY("SELECT * FROM ? WHERE ?", 2);

    private String sql;
    private int argsAmount;

    Statements(String sql, int argsAmount) {
        this.sql = sql;
        this.argsAmount = argsAmount;
    }

    public String getSql(String... args) {
        String statement = sql;
        int amount = args.length <= argsAmount ? args.length : argsAmount;
        for (int i = 0; i < amount; i++) {
            statement = statement.replaceFirst("\\?", Matcher.quoteReplacement(args[i]));
        }
        return statement;
    }

    public String getRawSql() {
        return sql;
    }

    public static String makeSqlSafe(String s) {
        return s.replaceAll("'", "\\'");
    }
}
