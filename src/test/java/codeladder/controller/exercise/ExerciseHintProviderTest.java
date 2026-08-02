package codeladder.controller.exercise;

import codeladder.data.ExerciseDataProvider;
import codeladder.model.Exercise;
import codeladder.model.InteractionType;
import codeladder.model.ValidationType;
import codeladder.support.ExerciseFixtures;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExerciseHintProviderTest {
    private final ExerciseHintProvider provider = new ExerciseHintProvider();

    @Test
    void openQuestionDoesNotShowHintButton() {
        assertFalse(provider.shouldShowHintButton(exercise(InteractionType.OPEN_QUESTION, ValidationType.TEXT_KEYWORDS)));
    }

    @Test
    void reflectionDoesNotShowHintButton() {
        assertFalse(provider.shouldShowHintButton(exercise(InteractionType.REFLECTION, ValidationType.REFLECTION)));
    }

    @Test
    void otherExistingInteractionTypesCanShowHintButton() {
        assertTrue(provider.shouldShowHintButton(exercise(InteractionType.MULTIPLE_CHOICE, ValidationType.SINGLE_CHOICE)));
        assertTrue(provider.shouldShowHintButton(exercise(InteractionType.MULTI_SELECT, ValidationType.MULTI_SELECT)));
        assertTrue(provider.shouldShowHintButton(exercise(InteractionType.CATEGORY_CHOICE, ValidationType.SINGLE_CHOICE)));
        assertTrue(provider.shouldShowHintButton(exercise(InteractionType.ERROR_ANALYSIS, ValidationType.MULTI_SELECT)));
        assertTrue(provider.shouldShowHintButton(exercise(InteractionType.FILL_IN_THE_BLANK, ValidationType.TEXT_KEYWORDS)));
    }

    @Test
    void programmingExercisesUseExplicitHintTextWhenPresent() {
        Exercise exercise = new ExerciseDataProvider().getExercises().stream()
                .filter(item -> "education-count-passing-01-goal".equals(item.getId()))
                .findFirst()
                .orElseThrow();
        assertTrue(provider.buildHintText(exercise).contains("Zoek in de case"));
    }

    private Exercise exercise(InteractionType interactionType, ValidationType validationType) {
        return ExerciseFixtures.createExercise("hint-" + interactionType, interactionType, validationType, List.of(), Set.of(), Set.of("a"), Set.of(), 1, "");
    }
}
