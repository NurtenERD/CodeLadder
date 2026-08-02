package codeladder.data;

import codeladder.model.Exercise;
import codeladder.model.InteractionType;
import codeladder.model.StructuredFieldDefinition;
import codeladder.model.StructuredFieldInputType;
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
            InteractionType type = exercise.getInteractionType();
            if (type == InteractionType.OPEN_QUESTION
                    || type == InteractionType.FILL_IN_THE_BLANK
                    || type == InteractionType.CODE_WRITING
                    || type == InteractionType.REFLECTION) {
                continue;
            }
            if (type == InteractionType.ANALYSIS_FORM) {
                assertStructuredFields(exercise);
                continue;
            }
            Set<String> optionIds = exercise.getOptions().stream().map(option -> option.getId()).collect(Collectors.toSet());
            assertFalse(optionIds.isEmpty(), "Options ontbreken voor " + exercise.getId());
            assertTrue(optionIds.containsAll(exercise.getCorrectOptionIds()), "Correct ids ontbreken in options voor " + exercise.getId());
            if (type == InteractionType.MULTIPLE_CHOICE || type == InteractionType.CATEGORY_CHOICE) {
                assertEquals(1, exercise.getCorrectOptionIds().size(), "Keuzevraag moet precies één correct antwoord hebben: " + exercise.getId());
            } else if (type == InteractionType.MULTI_SELECT || type == InteractionType.ERROR_ANALYSIS) {
                assertFalse(exercise.getCorrectOptionIds().isEmpty(), "Meerkeuzevraag mist correcte ids: " + exercise.getId());
            }
        }
    }

    private void assertStructuredFields(Exercise exercise) {
        assertTrue(exercise.getStructuredAnswerDefinition().isPresent(), "Analysis form mist structuredAnswerDefinition: " + exercise.getId());
        for (StructuredFieldDefinition field : exercise.getStructuredAnswerDefinition().orElseThrow().getFields()) {
            if (field.getInputType() == StructuredFieldInputType.TEXT) {
                continue;
            }
            Set<String> optionIds = field.getOptions().stream().map(option -> option.getId()).collect(Collectors.toSet());
            assertFalse(optionIds.isEmpty(), "Veldopties ontbreken voor " + exercise.getId() + ":" + field.getId());
            assertTrue(optionIds.containsAll(field.getCorrectOptionIds()), "Correcte veldids ontbreken voor " + exercise.getId() + ":" + field.getId());
        }
    }
}
