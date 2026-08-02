package codeladder.controller.dashboard;

import codeladder.model.StepType;

public interface DeveloperNavigation {
    void startAtStep(StepType stepType);
    void startAtExercise(String exerciseId);
}
