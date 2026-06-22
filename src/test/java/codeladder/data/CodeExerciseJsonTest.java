package codeladder.data;

import codeladder.model.Exercise;
import codeladder.model.ExerciseType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CodeExerciseJsonTest {

    @Test
    void codeWritingExercisesHaveRequiredFragmentsAndConsistentFlags() {
        ExerciseDataProvider provider = new ExerciseDataProvider();

        for (Exercise exercise : provider.getExercises()) {
            if (exercise.getExerciseType() == ExerciseType.CODE_WRITING || exercise.isCodeExercise()) {
                assertTrue(exercise.isCodeExercise(), "Code-oefening moet codeExercise=true hebben: " + exercise.getId());
                assertFalse(exercise.getRequiredFragments().isEmpty(), "Code-oefening mist requiredFragments: " + exercise.getId());
                assertTrue(
                        exercise.getMinimumRequiredMatches() <= exercise.getRequiredFragments().size(),
                        "minimumRequiredMatches is te groot voor " + exercise.getId()
                );
            }
        }
    }
}
