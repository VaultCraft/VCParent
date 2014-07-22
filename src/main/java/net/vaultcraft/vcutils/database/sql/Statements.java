package net.vaultcraft.vcutils.database.sql;


/**
 * Created by tacticalsk8er on 7/21/2014.
 */
public enum Statements {

    TABLE("CREATE TABLE IF NOT EXISTS ?(ID INT NOT NULL AUTO_INCREMENT, PRIMARY KEY(ID), ?)", 2),
    INSERT("INSERT INTO ? VALUES(default, ?)", 2);

    private String sql;
    private int argsAmount;

    Statements(String sql, int argsAmount) {
        this.sql = sql;
        this.argsAmount = argsAmount;
    }

    public String getSql(String... args) {
        int amount = args.length <= argsAmount ? args.length : argsAmount;
        for(int i = 0; i < amount; i++) {
            sql = sql.replaceFirst("\\?", args[i]);
        }
        return sql;
    }

    public String getRawSql() {
        return sql;
    }
}
