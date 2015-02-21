import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.permission.BukkitPermissionsFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class PermissionsTest {

    @Test
    public void ConfigYMLTest() throws Exception {
        File file = new File("test.yml");
        BukkitPermissionsFile bpf = new BukkitPermissionsFile(file);

        Assert.assertNotNull(bpf.permissions.get(Group.COMMON.getName()));
        file.deleteOnExit();
    }
}
