package codeladder.service;

import codeladder.data.ExerciseDataProvider;
import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.ValidationResult;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnswerValidationServiceTest {

    private final ExerciseDataProvider provider = new ExerciseDataProvider();
    private final AnswerValidationService validationService = new AnswerValidationService(new FeedbackService());

    @Test
    void wrongAnswerOnFirstAttemptAllowsRetry() {
        Exercise exercise = findExercise("gym-step1-best-sentence");

        ValidationResult result = validationService.validate(
                exercise,
                ExerciseResponse.forSelections(Set.of("technical")),
                1
        );

        assertFalse(result.isCorrect());
        assertTrue(result.isAllowRetry());
    }

    @Test
    void wrongAnswerOnSecondAttemptStopsRetry() {
        Exercise exercise = findExercise("gym-step1-best-sentence");

        ValidationResult result = validationService.validate(
                exercise,
                ExerciseResponse.forSelections(Set.of("technical")),
                2
        );

        assertFalse(result.isCorrect());
        assertFalse(result.isAllowRetry());
    }

    @Test
    void emptyAnswerIsNotCorrect() {
        Exercise exercise = findExercise("gym-step1-what");

        ValidationResult result = validationService.validate(
                exercise,
                ExerciseResponse.empty(),
                1
        );

        assertFalse(result.isCorrect());
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

    private Exercise findExercise(String id) {
        List<Exercise> exercises = provider.getExercises();
        return exercises.stream()
                .filter(exercise -> exercise.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }
}
