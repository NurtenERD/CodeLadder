package codeladder.data;

import codeladder.model.Exercise;
import codeladder.model.ExerciseType;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChoiceIntegrityTest {

    @Test
    void choiceExercisesHaveExistingCorrectOptionIds() {
        ExerciseDataProvider provider = new ExerciseDataProvider();

        for (Exercise exercise : provider.getExercises()) {
            if (exercise.getExerciseType() == ExerciseType.MULTIPLE_CHOICE
                    || exercise.getExerciseType() == ExerciseType.CATEGORY_CHOICE
                    || exercise.getExerciseType() == ExerciseType.MULTI_SELECT
                    || exercise.getExerciseType() == ExerciseType.ERROR_ANALYSIS) {
                Set<String> optionIds = exercise.getOptions().stream()
                        .map(option -> option.getId())
                        .collect(Collectors.toSet());

                assertFalse(optionIds.isEmpty(), "Options ontbreken voor " + exercise.getId());
                assertTrue(optionIds.containsAll(exercise.getCorrectOptionIds()), "Correct ids ontbreken in options voor " + exercise.getId());

                if (exercise.getExerciseType() == ExerciseType.MULTIPLE_CHOICE
                        || exercise.getExerciseType() == ExerciseType.CATEGORY_CHOICE) {
                    assertEquals(1, exercise.getCorrectOptionIds().size(), "Keuzevraag moet precies één correct antwoord hebben: " + exercise.getId());
                } else {
                    assertFalse(exercise.getCorrectOptionIds().isEmpty(), "Meerkeuzevraag mist correcte ids: " + exercise.getId());
                }
            }
        }
    }
}
