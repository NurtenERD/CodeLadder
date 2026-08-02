package codeladder.service;

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

class CodeFragmentValidationFeedbackTest {
    private final AnswerValidationService validationService = ValidationServiceFactory.create();

    @Test
    void classWithoutPrivateAttributesGetsRetryFeedback() {
        Exercise exercise = codeExercise(
                "test-write-user",
                Set.of("public class Gebruiker", "private String naam", "private String trainingsniveau"),
                3
        );
        ValidationResult result = validationService.validate(
                exercise,
                ExerciseResponse.forText("public class Gebruiker { String naam; String trainingsniveau; }"),
                1
        );
        assertFalse(result.isCorrect());
        assertTrue(result.isAllowRetry());
    }

    @Test
    void constructorWithoutThisAssignmentIsRejected() {
        Exercise exercise = codeExercise(
                "test-user-constructor",
                Set.of(
                        "public Gebruiker(String naam, String trainingsniveau)",
                        "this.naam = naam",
                        "this.trainingsniveau = trainingsniveau"
                ),
                3
        );
        ValidationResult result = validationService.validate(
                exercise,
                ExerciseResponse.forText("public Gebruiker(String naam, String trainingsniveau) { naam = naam; trainingsniveau = trainingsniveau; }"),
                1
        );
        assertFalse(result.isCorrect());
        assertTrue(result.getFeedback().getMessage().contains("this.naam = naam"));
    }

    @Test
    void objectWithoutNewIsRejected() {
        Exercise exercise = codeExercise("test-user-object", Set.of("Gebruiker gebruiker", "new Gebruiker", "\"Sara\"", "\"gevorderd\""), 4);
        ValidationResult result = validationService.validate(
                exercise,
                ExerciseResponse.forText("Gebruiker gebruiker = Gebruiker(\"Sara\", \"gevorderd\");"),
                1
        );
        assertFalse(result.isCorrect());
        assertTrue(result.getFeedback().getMessage().contains("`new`"));
    }

    @Test
    void missingPrivateAttributeGetsSpecificRetryFeedback() {
        Exercise exercise = codeExercise(
                "test-write-user",
                Set.of("public class Gebruiker", "private String naam", "private String trainingsniveau"),
                3
        );
        ValidationResult result = validationService.validate(
                exercise,
                ExerciseResponse.forText("public class Gebruiker { private String naam; }"),
                1
        );
        assertFalse(result.isCorrect());
        assertTrue(result.getFeedback().getMessage().contains("private String trainingsniveau"));
    }

    private Exercise codeExercise(String id, Set<String> requiredFragments, int minimumRequiredMatches) {
        return ExerciseFixtures.createExercise(
                id,
                InteractionType.CODE_WRITING,
                ValidationType.CODE_FRAGMENTS,
                List.of(),
                Set.of(),
                Set.of(),
                requiredFragments,
                minimumRequiredMatches,
                ""
        );
    }
}
