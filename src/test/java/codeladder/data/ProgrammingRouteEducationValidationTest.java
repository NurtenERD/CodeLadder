package codeladder.data;

import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.ValidationResult;
import codeladder.service.AnswerValidationService;
import codeladder.support.ValidationServiceFactory;
import codeladder.util.StructuredFieldValueCodec;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProgrammingRouteEducationValidationTest {
    private final AnswerValidationService validationService = ValidationServiceFactory.create();
    private final ExerciseDataProvider provider = new ExerciseDataProvider();

    @Test
    void exerciseOneValidatesTheCorrectGoal() {
        assertTrue(validate("education-count-passing-01-goal", ExerciseResponse.forSelections(Set.of("passing-count")), 1).isCorrect());
    }

    @Test
    void exerciseTwoValidatesInputAndOutput() {
        assertTrue(validate("education-count-passing-02-input-output", ExerciseResponse.forFields(Map.of(
                "input", "grades",
                "output", "passing-count"
        )), 1).isCorrect());
    }

    @Test
    void exerciseThreeValidatesTheAnalysisCard() {
        assertTrue(validate("education-count-passing-03-analysis-card", ExerciseResponse.forFields(Map.of(
                "goal", "goal-statement",
                "input", "input-statement",
                "output", "output-statement",
                "rule", "rule-statement"
        )), 1).isCorrect());
    }

    @Test
    void exerciseFourAcceptsBothCommaAndDotNotation() {
        assertTrue(validate("education-count-passing-04-rule", ExerciseResponse.forText("5,5 of hoger"), 1).isCorrect());
        assertTrue(validate("education-count-passing-04-rule", ExerciseResponse.forText("5.5 of hoger"), 1).isCorrect());
    }

    @Test
    void exerciseFiveAcceptsFivePointFiveAndFivePointSix() {
        ValidationResult result = validate("education-count-passing-05-boundary", ExerciseResponse.forFields(Map.of(
                "selectedGrades", StructuredFieldValueCodec.encodeMultiSelect(Set.of("grade-55", "grade-56")),
                "reason", "vanaf 5,5 telt 5,5 mee"
        )), 1);
        assertTrue(result.isCorrect());
    }

    @Test
    void exerciseSixAcceptsExactlyTheFourEdgeCases() {
        ValidationResult result = validate("education-count-passing-06-edge-cases", ExerciseResponse.forFields(Map.of(
                "edgeCases", StructuredFieldValueCodec.encodeMultiSelect(Set.of("empty", "none-passing", "exact-boundary", "all-passing")),
                "emptyExplanation", "0"
        )), 1);
        assertTrue(result.isCorrect());
    }

    @Test
    void exerciseSevenAcceptsGoodIndependentAnalysisAndRejectsWrongBoundary() {
        ValidationResult correct = validate("education-count-passing-07-independent", ExerciseResponse.forFields(Map.of(
                "goal", "het aantal studenten met minstens 70 punten",
                "input", "een lijst met toetsscores",
                "output", "het aantal geslaagde studenten",
                "rule", "een score telt mee vanaf 70 of hoger",
                "boundary", "70",
                "edgeCase", "lege lijst geeft 0",
                "pattern", "tellen met een voorwaarde"
        )), 1);
        ValidationResult wrongBoundary = validate("education-count-passing-07-independent", ExerciseResponse.forFields(Map.of(
                "goal", "het aantal studenten met minstens 70 punten",
                "input", "een lijst met toetsscores",
                "output", "het aantal geslaagde studenten",
                "rule", "een score telt mee vanaf 70 of hoger",
                "boundary", "71",
                "edgeCase", "lege lijst geeft 0",
                "pattern", "tellen met een voorwaarde"
        )), 1);
        assertTrue(correct.isCorrect());
        assertFalse(wrongBoundary.isCorrect());
    }

    private ValidationResult validate(String exerciseId, ExerciseResponse response, int attemptNumber) {
        Exercise exercise = provider.getExercises().stream()
                .filter(item -> exerciseId.equals(item.getId()))
                .findFirst()
                .orElseThrow();
        return validationService.validate(exercise, response, attemptNumber);
    }
}
