package codeladder.data;

import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.ValidationResult;
import codeladder.service.AnswerValidationService;
import codeladder.support.ValidationServiceFactory;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProgrammingRouteTransferValidationTest {
    private final AnswerValidationService validationService = ValidationServiceFactory.create();
    private final Exercise exercise = new ExerciseDataProvider().getExercises().stream()
            .filter(item -> "library-count-overdue-08-transfer".equals(item.getId()))
            .findFirst()
            .orElseThrow();

    @Test
    void transferExerciseRecognizesGreaterThanZeroPattern() {
        ValidationResult result = validationService.validate(exercise, ExerciseResponse.forFields(Map.of(
                "goal", "het aantal boeken dat te laat is",
                "input", "een lijst met te late dagen",
                "output", "het aantal te late boeken",
                "rule", "een boek telt mee als het groter dan 0 is",
                "boundary", "0 telt niet mee",
                "edgeCase", "leeg geeft 0",
                "pattern", "tellen met een voorwaarde"
        )), 1);
        assertTrue(result.isCorrect());
    }

    @Test
    void transferExerciseRejectsClaimThatZeroCounts() {
        ValidationResult result = validationService.validate(exercise, ExerciseResponse.forFields(Map.of(
                "goal", "het aantal boeken dat te laat is",
                "input", "een lijst met te late dagen",
                "output", "het aantal te late boeken",
                "rule", "een boek telt mee als het groter dan 0 is",
                "boundary", "0 telt mee",
                "edgeCase", "leeg geeft 0",
                "pattern", "tellen met een voorwaarde"
        )), 1);
        assertFalse(result.isCorrect());
    }
}
