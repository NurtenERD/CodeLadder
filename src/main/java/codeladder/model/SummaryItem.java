package codeladder.model;

public class SummaryItem {
    private final String stepTitle;
    private final String exerciseTitle;
    private final String answerText;
    private final String feedbackText;
    private final int attemptCount;

    public SummaryItem(String stepTitle, String exerciseTitle, String answerText, String feedbackText, int attemptCount) {
        this.stepTitle = stepTitle;
        this.exerciseTitle = exerciseTitle;
        this.answerText = answerText;
        this.feedbackText = feedbackText;
        this.attemptCount = attemptCount;
    }

    public String getStepTitle() {
        return stepTitle;
    }

    public String getExerciseTitle() {
        return exerciseTitle;
    }

    public String getAnswerText() {
        return answerText;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public int getAttemptCount() {
        return attemptCount;
    }
}
