package codeladder.model;

public class AttemptResult {
    private final int attemptNumber;
    private final ExerciseResponse response;
    private final Feedback feedback;
    private final boolean correct;

    public AttemptResult(int attemptNumber, ExerciseResponse response, Feedback feedback, boolean correct) {
        this.attemptNumber = attemptNumber;
        this.response = response;
        this.feedback = feedback;
        this.correct = correct;
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
}
