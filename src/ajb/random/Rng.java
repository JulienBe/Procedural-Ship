package ajb.random;

import java.util.Random;

/**
 * Created by JulienBe on 05/08/16.
 */
public class Rng extends Random {

    private static final Random rand = new Random("SeeD".hashCode());

    public static int anyRandomIntRange(int min, int max) {
        return min + rand.nextInt(max);
    }
    public static boolean aBoolean() { return rand.nextBoolean(); }
}
