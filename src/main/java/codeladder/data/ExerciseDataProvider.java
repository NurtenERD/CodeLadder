package codeladder.data;

import codeladder.model.Exercise;
import codeladder.model.LearningStep;
import codeladder.model.StepType;

import java.util.Arrays;
import java.util.List;

public class ExerciseDataProvider {
    private final List<Exercise> exercises;

    public ExerciseDataProvider() {
        this(new ExerciseJsonLoader());
    }

    ExerciseDataProvider(ExerciseJsonLoader exerciseJsonLoader) {
        this.exercises = List.copyOf(exerciseJsonLoader.loadExercises());
    }

    public List<LearningStep> getLearningSteps() {
        return Arrays.stream(StepType.values())
                .map(LearningStep::new)
                .toList();
    }

    public List<Exercise> getExercises() {
        return exercises;
    }
}
