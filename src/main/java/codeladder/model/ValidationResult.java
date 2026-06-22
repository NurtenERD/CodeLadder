package codeladder.model;

public class ValidationResult {
    private final boolean correct;
    private final boolean allowRetry;
    private final Feedback feedback;

    public ValidationResult(boolean correct, boolean allowRetry, Feedback feedback) {
        this.correct = correct;
        this.allowRetry = allowRetry;
        this.feedback = feedback;
    }

    public boolean isCorrect() {
        return correct;
    }

    public boolean isAllowRetry() {
        return allowRetry;
    }

    public Feedback getFeedback() {
        return feedback;
    }
}
