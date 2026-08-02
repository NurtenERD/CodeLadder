package codeladder.model;

public class AttemptResult {
    private final int attemptNumber;
    private final ExerciseResponse response;
    private final Feedback feedback;
    private final boolean correct;
    private final ValidationDetails details;

    public AttemptResult(int attemptNumber, ExerciseResponse response, Feedback feedback, boolean correct, ValidationDetails details) {
        this.attemptNumber = attemptNumber;
        this.response = response;
        this.feedback = feedback;
        this.correct = correct;
        this.details = details == null ? ValidationDetails.empty() : details;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public ExerciseResponse getResponse() {
        return response;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public boolean isCorrect() {
        return correct;
    }

    public ValidationDetails getDetails() {
        return details;
    }
}
