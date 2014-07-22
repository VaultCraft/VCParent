package net.vaultcraft.vcutils.protection.flag;

import net.vaultcraft.vcutils.user.Group;

/**
 * Created by Connor on 7/21/14. Designed for the VCUtils project.
 */

public class FlagResult {

    private Group allow;
    private boolean cancel;

    public FlagResult(Group allow, boolean cancel) {
        this.allow = allow;
        this.cancel = cancel;
    }

    public Group getAllowed() {
        return allow;
    }

    public boolean isCancelled() {
        return cancel;
    }
}
