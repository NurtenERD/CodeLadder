package codeladder.service;

import codeladder.data.ExerciseDataProvider;
import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.SummaryItem;
import codeladder.model.ValidationResult;
import codeladder.support.ValidationServiceFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SummaryServiceTest {

    @Test
    void summaryContainsRecordedAnswers() {
        ExerciseDataProvider provider = new ExerciseDataProvider();
        AnswerValidationService validationService = ValidationServiceFactory.create();
        ProgressService progressService = new ProgressService();
        SummaryService summaryService = new SummaryService();

        Exercise exercise = provider.getExercises().getFirst();
        ExerciseResponse response = ExerciseResponse.forText("De gebruiker bekijkt oefeningen en maakt een weekschema.");
        ValidationResult validationResult = validationService.validate(exercise, response, 1);
        progressService.recordAttempt(exercise, response, validationResult);

        List<SummaryItem> items = summaryService.buildSummary(provider.getExercises(), progressService.getAllAnswers());

        assertFalse(items.isEmpty());
        assertTrue(items.stream().anyMatch(item -> item.getExerciseTitle().equals(exercise.getTitle())));
    }
}
