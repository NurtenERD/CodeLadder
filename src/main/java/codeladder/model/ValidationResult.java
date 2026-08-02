package codeladder.model;

public class ValidationResult {
    private final boolean correct;
    private final boolean allowRetry;
    private final Feedback feedback;
    private final ValidationDetails details;

    public ValidationResult(boolean correct, boolean allowRetry, Feedback feedback) {
        this(correct, allowRetry, feedback, ValidationDetails.empty());
    }

    public ValidationResult(boolean correct, boolean allowRetry, Feedback feedback, ValidationDetails details) {
        this.correct = correct;
        this.allowRetry = allowRetry;
        this.feedback = feedback;
        this.details = details == null ? ValidationDetails.empty() : details;
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

    public ValidationDetails getDetails() {
        return details;
    }
}
