package codeladder.service;

import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.InteractionType;
import codeladder.model.ValidationType;
import codeladder.support.ExerciseFixtures;
import codeladder.support.ValidationServiceFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CodeFragmentValidationSuccessTest {
    private final AnswerValidationService validationService = ValidationServiceFactory.create();

    @Test
    void constructorWithBothThisAssignmentsIsApproved() {
        Exercise exercise = codeExercise(
                "test-user-constructor",
                Set.of(
                        "public Gebruiker(String naam, String trainingsniveau)",
                        "this.naam = naam",
                        "this.trainingsniveau = trainingsniveau"
                ),
                3
        );
        assertTrue(validationService.validate(
                exercise,
                ExerciseResponse.forText("""
                        public Gebruiker(String naam, String trainingsniveau){
                            this.naam = naam;
                            this.trainingsniveau = trainingsniveau;
                        }
                        """),
                1
        ).isCorrect());
    }

    @Test
    void objectWithNewIsApproved() {
        Exercise exercise = codeExercise("test-user-object", Set.of("Gebruiker gebruiker", "new Gebruiker", "\"Sara\"", "\"gevorderd\""), 4);
        assertTrue(validationService.validate(
                exercise,
                ExerciseResponse.forText("Gebruiker gebruiker=new Gebruiker(\"Sara\",\"gevorderd\")"),
                1
        ).isCorrect());
    }

    @Test
    void methodCallWithDotNotationIsApproved() {
        Exercise exercise = codeExercise("test-method-call", Set.of("gebruiker.toonProfiel()"), 1);
        assertTrue(validationService.validate(exercise, ExerciseResponse.forText("gebruiker.toonProfiel();"), 1).isCorrect());
    }

    @Test
    void whitespaceDifferencesDoNotBreakCodeValidation() {
        Exercise exercise = codeExercise(
                "test-write-user",
                Set.of("public class Gebruiker", "private String naam", "private String trainingsniveau"),
                3
        );
        assertTrue(validationService.validate(
                exercise,
                ExerciseResponse.forText("public class Gebruiker{ private String naam ; private String trainingsniveau ; }"),
                1
        ).isCorrect());
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
