package codeladder.service;

import codeladder.model.AttemptResult;
import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.StudentAnswer;
import codeladder.model.ValidationResult;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProgressService {
    private final Map<String, StudentAnswer> answersByExerciseId = new LinkedHashMap<>();
    private String reflection = "";

    public void recordAttempt(Exercise exercise, ExerciseResponse response, ValidationResult validationResult) {
        StudentAnswer studentAnswer = answersByExerciseId.computeIfAbsent(
                exercise.getId(),
                ignored -> new StudentAnswer(exercise.getId(), exercise.getTitle(), exercise.getStepType())
        );

        AttemptResult attemptResult = new AttemptResult(
                studentAnswer.getAttemptCount() + 1,
                response,
                validationResult.getFeedback(),
                validationResult.isCorrect()
        );
        studentAnswer.addAttempt(attemptResult);
    }

    public StudentAnswer getStudentAnswer(String exerciseId) {
        return answersByExerciseId.get(exerciseId);
    }

    public int getAttemptCount(String exerciseId) {
        StudentAnswer studentAnswer = answersByExerciseId.get(exerciseId);
        return studentAnswer == null ? 0 : studentAnswer.getAttemptCount();
    }

    public Collection<StudentAnswer> getAllAnswers() {
        return answersByExerciseId.values();
    }

    public void saveReflection(String reflection) {
        this.reflection = reflection == null ? "" : reflection.trim();
    }

    public String getReflection() {
        return reflection;
    }

    public void reset() {
        answersByExerciseId.clear();
        reflection = "";
    }
}
