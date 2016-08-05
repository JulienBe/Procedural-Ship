package ajb.domain;

/**
 * Created by julien on 05/08/16.
 */
public enum Parameters {

    PREVIOUS(0.25f, 0),
    MINE(     0.5f, 0.1f);

    public float colorMaxPercentage, colorMinPercentage;

    Parameters(float colorMaxPercentage, float colorMinPercentage) {
        this.colorMaxPercentage = colorMaxPercentage;
        this.colorMinPercentage = colorMinPercentage;
    }
}
