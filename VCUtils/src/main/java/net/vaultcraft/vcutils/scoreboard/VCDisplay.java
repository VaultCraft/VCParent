package net.vaultcraft.vcutils.scoreboard;

/**
 * Created by tacticalsk8er on 7/26/2014.
 */
public enum VCDisplay {

    LIST(0),
    SIDEBAR(1),
    BELOWNAME(2);

    private int id;

    private VCDisplay(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
