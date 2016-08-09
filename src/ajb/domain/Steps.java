package ajb.domain;

/**
 * Created by julein on 05/08/16.
 */
public enum Steps {

    random(5, 50,  5, 50),
    SMALL(5, 15,  5, 30),
    MEDIUM(10, 30, 10, 40),
    large(20, 50, 15, 50);

    public final int minSteps, maxSteps, minSubStep,maxSubSteps;

    Steps(int minSteps, int maxSteps, int minSubStep, int maxSubSteps) {
        this.minSteps = minSteps;
        this.maxSteps = maxSteps;
        this.minSubStep = minSubStep;
        this.maxSubSteps = maxSubSteps;
    }
}
