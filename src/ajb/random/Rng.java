package ajb.random;

import java.util.Random;

/**
 * Created by JulienBe on 05/08/16.
 */
public class Rng extends Random {

    private static final Random rand = new Random("SeeD".hashCode());

    public static int intBetween(int min, int max) { return min + rand.nextInt(max); }
    public static int anInt(int max) { return rand.nextInt(max); }
    public static boolean aBoolean() { return rand.nextBoolean(); }
    public static float aFloat() { return rand.nextFloat(); }
}
