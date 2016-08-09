package ajb.domain;

/**
 * Created by julien on 05/08/16.
 */
public enum Parameters {

    PREVIOUS(0.25f, 0, 0f, 0.1f, 0.1f, 0.8f, 0.5f, 0, 0, 300),
    MINE(     0.55f, 0.10f, .5f, 1f, 2f, 5f, 0.1f, 5, 30, 50);

    /**
     * It is influenced by a line kept parameter that will decrease overtime the possibility that the line is kept
     */
    public float tendancyToKeepLine;
    public float colorMaxPercentage, colorMinPercentage, tendencyToBeWide, streakMul;
    public int blackNoisePercentage, greyNoisePercentage, colorNoisePercentage, minHeight, maxHeight;

    Parameters(float colorMaxPercentage, float colorMinPercentage, float tendancyToKeepLine, float blackNoisePercentage, float greyNoisePercentage, float colorPercentage, float tendencyToBeWide, int streakMul, int minHeight, int maxHeight) {
        this.colorMaxPercentage = colorMaxPercentage;
        this.colorMinPercentage = colorMinPercentage;
        this.tendancyToKeepLine = tendancyToKeepLine;
        float percentageTotal = blackNoisePercentage + greyNoisePercentage + colorPercentage;
        this.blackNoisePercentage = (int) ((blackNoisePercentage / percentageTotal) * 100);
        this.colorNoisePercentage = (int) ((colorPercentage / percentageTotal) * 100);
        this.greyNoisePercentage = (int) ((greyNoisePercentage / percentageTotal) * 100);
        this.tendencyToBeWide = tendencyToBeWide;
        this.streakMul = streakMul;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }
}
