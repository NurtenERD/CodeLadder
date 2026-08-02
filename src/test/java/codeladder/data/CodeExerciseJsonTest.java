package codeladder.data;

import codeladder.model.Exercise;
import codeladder.model.ValidationType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CodeExerciseJsonTest {

    @Test
    void codeFragmentExercisesHaveRequiredFragmentsAndConsistentThreshold() {
        ExerciseDataProvider provider = new ExerciseDataProvider();
        for (Exercise exercise : provider.getExercises()) {
            if (exercise.getValidationType() != ValidationType.CODE_FRAGMENTS) {
                continue;
            }
            assertFalse(exercise.getRequiredFragments().isEmpty(), "Code-oefening mist requiredFragments: " + exercise.getId());
            assertTrue(
                    exercise.getMinimumRequiredMatches() <= exercise.getRequiredFragments().size(),
                    "minimumRequiredMatches is te groot voor " + exercise.getId()
            );
        }
    }
}
