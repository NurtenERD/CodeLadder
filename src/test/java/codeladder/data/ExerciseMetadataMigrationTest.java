package codeladder.data;

import codeladder.model.Exercise;
import codeladder.model.InteractionType;
import codeladder.model.ValidationType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExerciseMetadataMigrationTest {

    @Test
    void migratedInteractionTypesUseExpectedValidationTypes() {
        ExerciseDataProvider provider = new ExerciseDataProvider();
        for (Exercise exercise : provider.getExercises()) {
            assertEquals(expectedValidation(exercise.getInteractionType()), exercise.getValidationType(), "ValidationType mismatch voor " + exercise.getId());
        }
    }

    private ValidationType expectedValidation(InteractionType type) {
        return switch (type) {
            case OPEN_QUESTION, FILL_IN_THE_BLANK -> ValidationType.TEXT_KEYWORDS;
            case MULTIPLE_CHOICE, CATEGORY_CHOICE -> ValidationType.SINGLE_CHOICE;
            case MULTI_SELECT, ERROR_ANALYSIS -> ValidationType.MULTI_SELECT;
            case CODE_WRITING -> ValidationType.CODE_FRAGMENTS;
            case REFLECTION -> ValidationType.REFLECTION;
            case ORDER_ITEMS -> ValidationType.ORDERED_ITEMS;
            case ANALYSIS_FORM -> ValidationType.STRUCTURED_FIELDS;
        };
    }
}
