package codeladder.controller.exercise;

import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.InteractionType;

import java.util.Set;

public interface ExerciseInputRenderer {
    Set<InteractionType> getSupportedTypes();
    ExerciseInputView render(Exercise exercise, ExerciseResponse initialResponse);
}
