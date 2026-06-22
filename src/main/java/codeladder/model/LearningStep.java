package codeladder.model;

public class LearningStep {
    private final StepType stepType;

    public LearningStep(StepType stepType) {
        this.stepType = stepType;
    }

    public StepType getStepType() {
        return stepType;
    }

    public int getOrderNumber() {
        return stepType.getOrderNumber();
    }

    public String getTitle() {
        return stepType.getShortTitle();
    }

    public String getDescription() {
        return stepType.getDescription();
    }
}
