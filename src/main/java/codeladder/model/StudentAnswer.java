package codeladder.model;

import java.util.ArrayList;
import java.util.List;

public class StudentAnswer {
    private final String exerciseId;
    private final String exerciseTitle;
    private final StepType stepType;
    private final List<AttemptResult> attempts;

    public StudentAnswer(String exerciseId, String exerciseTitle, StepType stepType) {
        this.exerciseId = exerciseId;
        this.exerciseTitle = exerciseTitle;
        this.stepType = stepType;
        this.attempts = new ArrayList<>();
    }

    public void addAttempt(AttemptResult attemptResult) {
        attempts.add(attemptResult);
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public String getExerciseTitle() {
        return exerciseTitle;
    }

    public StepType getStepType() {
        return stepType;
    }

    public List<AttemptResult> getAttempts() {
        return new ArrayList<>(attempts);
    }

    public AttemptResult getLatestAttempt() {
        return attempts.get(attempts.size() - 1);
    }

    public ExerciseResponse getLatestResponse() {
        return getLatestAttempt().getResponse();
    }

    public int getAttemptCount() {
        return attempts.size();
    }

    public boolean isReadyForNext() {
        return !attempts.isEmpty() && (getLatestAttempt().isCorrect() || attempts.size() >= 2);
    }
}
