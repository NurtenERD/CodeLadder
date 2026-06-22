package codeladder.service;

import codeladder.model.CaseStudy;
import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.ExerciseType;
import codeladder.model.StepType;
import codeladder.model.ValidationResult;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CodeValidationTest {

    private static final CaseStudy CASE_STUDY = new CaseStudy(
            "GymApp-hoofdroute",
            "GymApp",
            "Bouw een app voor een sportschool."
    );

    private final AnswerValidationService validationService = new AnswerValidationService(new FeedbackService());

    @Test
    void goodGebruikerClassIsApproved() {
        Exercise exercise = createCodeExercise(
                "test-write-user",
                Set.of("public class Gebruiker", "private String naam", "private String trainingsniveau"),
                3
        );

        ValidationResult result = validationService.validate(
                exercise,
                ExerciseResponse.forText("""
                        public class Gebruiker {
                            private String naam;
                            private String trainingsniveau;
                        }
                        """),
                1
        );

        assertTrue(result.isCorrect());
    }

    @Test
    void classWithoutPrivateAttributesGetsRetryFeedback() {
        Exercise exercise = createCodeExercise(
                "test-write-user",
                Set.of("public class Gebruiker", "private String naam", "private String trainingsniveau"),
                3
        );

        ValidationResult result = validationService.validate(
                exercise,
                ExerciseResponse.forText("""
                        public class Gebruiker {
                            String naam;
                            String trainingsniveau;
                        }
                        """),
                1
        );

        assertFalse(result.isCorrect());
        assertTrue(result.isAllowRetry());
    }

    @Test
    void constructorWithoutThisAssignmentIsRejected() {
        Exercise exercise = createCodeExercise(
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
                ExerciseResponse.forText("""
                        public Gebruiker(String naam, String trainingsniveau) {
                            naam = naam;
                            trainingsniveau = trainingsniveau;
                        }
                        """),
                1
        );

        assertFalse(result.isCorrect());
        assertTrue(result.getFeedback().getMessage().contains("this.naam = naam"));
    }

    @Test
    void constructorWithBothThisAssignmentsIsApproved() {
        Exercise exercise = createCodeExercise(
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
                ExerciseResponse.forText("""
                        public Gebruiker(String naam, String trainingsniveau){
                            this.naam = naam;
                            this.trainingsniveau = trainingsniveau;
                        }
                        """),
                1
        );

        assertTrue(result.isCorrect());
    }

    @Test
    void objectWithoutNewIsRejected() {
        Exercise exercise = createCodeExercise(
                "test-user-object",
                Set.of("Gebruiker gebruiker", "new Gebruiker", "\"Sara\"", "\"gevorderd\""),
                4
        );

        ValidationResult result = validationService.validate(
                exercise,
                ExerciseResponse.forText("Gebruiker gebruiker = Gebruiker(\"Sara\", \"gevorderd\");"),
                1
        );

        assertFalse(result.isCorrect());
        assertTrue(result.getFeedback().getMessage().contains("`new`"));
    }

    @Test
    void objectWithNewIsApproved() {
        Exercise exercise = createCodeExercise(
                "test-user-object",
                Set.of("Gebruiker gebruiker", "new Gebruiker", "\"Sara\"", "\"gevorderd\""),
                4
        );

        ValidationResult result = validationService.validate(
                exercise,
                ExerciseResponse.forText("Gebruiker gebruiker=new Gebruiker(\"Sara\",\"gevorderd\")"),
                1
        );

        assertTrue(result.isCorrect());
    }

    @Test
    void methodCallWithDotNotationIsApproved() {
        Exercise exercise = createCodeExercise(
                "test-method-call",
                Set.of("gebruiker.toonProfiel()"),
                1
        );

        ValidationResult result = validationService.validate(
                exercise,
                ExerciseResponse.forText("gebruiker.toonProfiel();"),
                1
        );

        assertTrue(result.isCorrect());
    }

    @Test
    void whitespaceDifferencesDoNotImmediatelyBreakCodeValidation() {
        Exercise exercise = createCodeExercise(
                "test-write-user",
                Set.of("public class Gebruiker", "private String naam", "private String trainingsniveau"),
                3
        );

        ValidationResult result = validationService.validate(
                exercise,
                ExerciseResponse.forText("public class Gebruiker{ private String naam ; private String trainingsniveau ; }"),
                1
        );

        assertTrue(result.isCorrect());
    }

    @Test
    void missingPrivateAttributeGetsSpecificRetryFeedback() {
        Exercise exercise = createCodeExercise(
                "test-write-user",
                Set.of("public class Gebruiker", "private String naam", "private String trainingsniveau"),
                3
        );

        ValidationResult result = validationService.validate(
                exercise,
                ExerciseResponse.forText("""
                        public class Gebruiker {
                            private String naam;
                        }
                        """),
                1
        );

        assertFalse(result.isCorrect());
        assertTrue(result.isAllowRetry());
        assertNotNull(result.getFeedback().getMessage());
        assertTrue(result.getFeedback().getMessage().contains("private String trainingsniveau"));
    }

    private Exercise createCodeExercise(String id, Set<String> requiredFragments, int minimumRequiredMatches) {
        return new Exercise(
                id,
                StepType.WRITE_SMALL_CLASSES,
                ExerciseType.CODE_WRITING,
                "Code-oefening",
                "Controleer de belangrijkste code-onderdelen.",
                "Schrijf of herken de juiste code.",
                "",
                CASE_STUDY,
                List.of(),
                Set.of(),
                Set.of(),
                requiredFragments,
                minimumRequiredMatches,
                true,
                "Goed gedaan.",
                "Kijk nog eens naar de ontbrekende code.",
                "Neem mee welke code-onderdelen hier nodig zijn."
        );
    }
}
