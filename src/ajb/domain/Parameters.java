package ajb.domain;

/**
 * Created by julien on 05/08/16.
 */
public enum Parameters {

    PREVIOUS(0.25f, 0, 0f, 0.1f, 0.1f, 0.8f, 0.5f, 0),
    MINE(     0.75f, 0.25f, .7f, 1f, 2f, 5f, 0.1f, 6);

    public float colorMaxPercentage, colorMinPercentage, tendancyToKeepLine, tendencyToBeWide, streakMul;
    public int blackNoisePercentage, greyNoisePercentage, colorNoisePercentage;

    Parameters(float colorMaxPercentage, float colorMinPercentage, float tendancyToKeepLine, float blackNoisePercentage, float greyNoisePercentage, float colorPercentage, float tendencyToBeWide, int streakMul) {
        this.colorMaxPercentage = colorMaxPercentage;
        this.colorMinPercentage = colorMinPercentage;
        this.tendancyToKeepLine = tendancyToKeepLine;
        float percentageTotal = blackNoisePercentage + greyNoisePercentage + colorPercentage;
        this.blackNoisePercentage = (int) ((blackNoisePercentage / percentageTotal) * 100);
        this.colorNoisePercentage = (int) ((colorPercentage / percentageTotal) * 100);
        this.greyNoisePercentage = (int) ((greyNoisePercentage / percentageTotal) * 100);
        this.tendencyToBeWide = tendencyToBeWide;
        this.streakMul = streakMul;
    }
}
