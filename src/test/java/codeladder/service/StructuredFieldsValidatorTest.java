package codeladder.service;

import codeladder.data.ExerciseDataProvider;
import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.SkillTag;
import codeladder.model.ValidationResult;
import codeladder.support.ValidationServiceFactory;
import codeladder.util.StructuredFieldValueCodec;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StructuredFieldsValidatorTest {
    private final AnswerValidationService validationService = ValidationServiceFactory.create();

    @Test
    void fullyCorrectFormIsAccepted() {
        Exercise exercise = findExercise("education-count-passing-02-input-output");
        ValidationResult result = validationService.validate(exercise, ExerciseResponse.forFields(Map.of(
                "input", "grades",
                "output", "passing-count"
        )), 1);
        assertTrue(result.isCorrect());
        assertTrue(result.getFeedback().getMessage().contains("opdrachtanalyse aangetoond"));
    }

    @Test
    void oneIncorrectFieldKeepsFormIncorrect() {
        Exercise exercise = findExercise("education-count-passing-02-input-output");
        ValidationResult result = validationService.validate(exercise, ExerciseResponse.forFields(Map.of(
                "input", "grades",
                "output", "teacher"
        )), 1);
        assertFalse(result.isCorrect());
        assertTrue(result.isAllowRetry());
    }

    @Test
    void firstFeedbackNamesWrongFieldWithoutShowingModelAnswer() {
        Exercise exercise = findExercise("education-count-passing-05-boundary");
        ValidationResult result = validationService.validate(exercise, ExerciseResponse.forFields(Map.of(
                "selectedGrades", StructuredFieldValueCodec.encodeMultiSelect(Set.of("grade-55", "grade-56")),
                "reason", "omdat het zo is"
        )), 1);
        assertTrue(result.getFeedback().getMessage().contains("Al goed: Cijfers die meetellen."));
        assertTrue(result.getFeedback().getMessage().contains("Gebruik het woord “vanaf”"));
        assertFalse(result.getFeedback().getMessage().contains("5,5 telt mee, omdat"));
    }

    @Test
    void secondFeedbackShowsOnlyModelAnswerForWrongField() {
        Exercise exercise = findExercise("education-count-passing-05-boundary");
        ValidationResult result = validationService.validate(exercise, ExerciseResponse.forFields(Map.of(
                "selectedGrades", StructuredFieldValueCodec.encodeMultiSelect(Set.of("grade-55", "grade-56")),
                "reason", "onduidelijk"
        )), 2);
        assertTrue(result.getFeedback().getMessage().contains("Al goed: Cijfers die meetellen."));
        assertTrue(result.getFeedback().getMessage().contains("5,5 telt mee, omdat"));
        assertFalse(result.getFeedback().getMessage().contains("5,5 en 5,6 tellen mee. 5,4 is lager dan de grens."));
    }

    @Test
    void fieldValidationOutcomeContainsCorrectSkillTag() {
        Exercise exercise = findExercise("education-count-passing-05-boundary");
        ValidationResult result = validationService.validate(exercise, ExerciseResponse.forFields(Map.of(
                "selectedGrades", StructuredFieldValueCodec.encodeMultiSelect(Set.of("grade-55", "grade-56")),
                "reason", "vanaf 5,5 telt 5,5 mee"
        )), 1);
        assertEquals(SkillTag.BOUNDARY, result.getDetails().getFieldOutcomes().get("reason").getSkillTag());
        assertTrue(result.getDetails().getFieldOutcomes().get("reason").isCorrect());
    }

    private Exercise findExercise(String id) {
        return new ExerciseDataProvider().getExercises().stream()
                .filter(exercise -> id.equals(exercise.getId()))
                .findFirst()
                .orElseThrow();
    }
}
