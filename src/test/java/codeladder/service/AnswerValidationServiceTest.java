package codeladder.service;

import codeladder.data.ExerciseDataProvider;
import codeladder.model.AnswerOption;
import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.InteractionType;
import codeladder.model.ValidationResult;
import codeladder.model.ValidationType;
import codeladder.support.ExerciseFixtures;
import codeladder.support.ValidationServiceFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnswerValidationServiceTest {
    private final ExerciseDataProvider provider = new ExerciseDataProvider();
    private final AnswerValidationService validationService = ValidationServiceFactory.create();

    @Test
    void wrongAnswerOnFirstAttemptAllowsRetry() {
        Exercise exercise = findExercise("gym-step1-best-sentence");
        ValidationResult result = validationService.validate(exercise, ExerciseResponse.forSelections(Set.of("technical")), 1);
        assertFalse(result.isCorrect());
        assertTrue(result.isAllowRetry());
    }

    @Test
    void wrongAnswerOnSecondAttemptStopsRetry() {
        Exercise exercise = findExercise("gym-step1-best-sentence");
        ValidationResult result = validationService.validate(exercise, ExerciseResponse.forSelections(Set.of("technical")), 2);
        assertFalse(result.isCorrect());
        assertFalse(result.isAllowRetry());
    }

    @Test
    void goodOpenAnswerIsMarkedCorrect() {
        Exercise exercise = findExercise("gym-step1-what");
        ValidationResult result = validationService.validate(
                exercise,
                ExerciseResponse.forText("De gebruiker bekijkt oefeningen en maakt een weekschema."),
                1
        );
        assertTrue(result.isCorrect());
    }

    @Test
    void singleChoiceValidationWorks() {
        Exercise exercise = ExerciseFixtures.createExercise(
                "single-choice",
                InteractionType.MULTIPLE_CHOICE,
                ValidationType.SINGLE_CHOICE,
                List.of(new AnswerOption("a", "A"), new AnswerOption("b", "B")),
                Set.of("a"),
                Set.of(),
                Set.of(),
                0,
                ""
        );
        assertTrue(validationService.validate(exercise, ExerciseResponse.forSelections(Set.of("a")), 1).isCorrect());
    }

    @Test
    void multiSelectValidationWorks() {
        Exercise exercise = ExerciseFixtures.createExercise(
                "multi-select",
                InteractionType.MULTI_SELECT,
                ValidationType.MULTI_SELECT,
                List.of(new AnswerOption("a", "A"), new AnswerOption("b", "B"), new AnswerOption("c", "C")),
                Set.of("a", "c"),
                Set.of(),
                Set.of(),
                0,
                ""
        );
        assertTrue(validationService.validate(exercise, ExerciseResponse.forSelections(Set.of("a", "c")), 1).isCorrect());
    }

    private Exercise findExercise(String id) {
        return provider.getExercises().stream()
                .filter(exercise -> exercise.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }
}
