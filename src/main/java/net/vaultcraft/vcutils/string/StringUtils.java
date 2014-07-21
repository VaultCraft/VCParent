package net.vaultcraft.vcutils.string;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class StringUtils {

    public static String buildFromArray(String[] array) {
        return buildFromArray(array, 0);
    }

    public static String buildFromArray(String[] array, int start) {
        String compose = "";
        for (int x = start; x < array.length; x++) {
            compose+=array[x]+" ";
        }
        return compose;
    }
}
