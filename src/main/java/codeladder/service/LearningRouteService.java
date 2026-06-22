package codeladder.service;

import codeladder.data.ExerciseDataProvider;
import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.LearningStep;
import codeladder.model.ValidationResult;

import java.util.List;

public class LearningRouteService {
    private final List<Exercise> exercises;
    private final List<LearningStep> learningSteps;
    private final AnswerValidationService answerValidationService;
    private int currentExerciseIndex;

    public LearningRouteService(ExerciseDataProvider dataProvider, AnswerValidationService answerValidationService) {
        this.exercises = dataProvider.getExercises();
        this.learningSteps = dataProvider.getLearningSteps();
        this.answerValidationService = answerValidationService;
        this.currentExerciseIndex = 0;
    }

    public List<LearningStep> getLearningSteps() {
        return learningSteps;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public Exercise getCurrentExercise() {
        if (currentExerciseIndex < 0 || currentExerciseIndex >= exercises.size()) {
            return null;
        }
        return exercises.get(currentExerciseIndex);
    }

    public int getCurrentExerciseNumber() {
        return currentExerciseIndex + 1;
    }

    public int getTotalExercises() {
        return exercises.size();
    }

    public void restart() {
        currentExerciseIndex = 0;
    }

    public ValidationResult validateCurrentExercise(ExerciseResponse response, int attemptNumber) {
        return answerValidationService.validate(getCurrentExercise(), response, attemptNumber);
    }

    public boolean moveToNextExercise() {
        currentExerciseIndex++;
        return currentExerciseIndex < exercises.size();
    }
}
