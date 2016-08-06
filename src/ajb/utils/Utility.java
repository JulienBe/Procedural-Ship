package ajb.utils;

/**
 * Created by julein on 06/08/16.
 */
public class Utility {

    public static int countTrues(boolean... booleans) {
        int cpt = 0;
        for (boolean b : booleans)
            if (b)
                cpt++;
        return cpt;
    }

}
