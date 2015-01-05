package net.vaultcraft.vcutils.user.permission;

/**
 * @author Connor Hollasch
 * @since 12/26/2014
 */
public enum Permission {

    BUILD_MAP(1);

    private int node;

    private Permission(int node) {
        this.node = node;
    }

    public int getPermissionNode() {
        return node;
    }

    public static Permission fromId(int i) {
        for (Permission x : values()) {
            if (x.node == i)
                return x;
        }
        return null;
    }
}
