import java.util.Arrays;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */

public class Test {
    public static void main(String[] args) {
        String[] orig = {"craftfestmc", "hello", "kai!"};
        String[] make = new String[orig.length-1];
        System.arraycopy(orig, 1, make, 0, make.length);
        System.out.println(Arrays.toString(make));
    }
}
