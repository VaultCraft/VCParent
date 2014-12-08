package net.vaultcraft.vcessentials.guild;

/**
 * Created by tacticalsk8er on 11/15/2014.
 */
public enum Role {

    CREATOR(100),
    LEADER(90),
    OFFICER(80),
    TRUSTWORTHY(70),
    MEMBER(60);

    private int permLevel;

    private Role(int permLevel) {
        this.permLevel = permLevel;
    }

    public int getPermissionLevel() {
        return permLevel;
    }

    public boolean hasPermission(Role other) {
        return permLevel >= other.permLevel;
    }

    public Role fromPermissionLevel(int level) {
        for (Role role : values()) {
            if (role.permLevel == level)
                return role;
        }

        return null;
    }
}
