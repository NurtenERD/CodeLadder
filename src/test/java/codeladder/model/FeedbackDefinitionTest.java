package codeladder.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FeedbackDefinitionTest {

    @Test
    void returnsFeedbackValuesAndExerciseDelegatesThem() {
        FeedbackDefinition definition = new FeedbackDefinition("Goed", "Opnieuw", "Definitief");
        Exercise exercise = new Exercise(
                "id",
                StepType.UNDERSTAND_ASSIGNMENT,
                metadata(),
                new ExerciseContent("Titel", "Instructie", "Vraag", "", "", caseStudy()),
                new AnswerDefinition(null, null, null, null, null, 0),
                definition,
                null
        );

        assertEquals("Goed", definition.getSuccessFeedback());
        assertEquals("Opnieuw", definition.getRetryFeedback());
        assertEquals("Definitief", definition.getFinalFeedback());
        assertSame(definition, exercise.getFeedbackDefinition());
        assertEquals("Goed", exercise.getSuccessFeedback());
        assertEquals("Opnieuw", exercise.getRetryFeedback());
        assertEquals("Definitief", exercise.getFinalFeedback());
    }

    @Test
    void rejectsNullForRequiredValues() {
        assertThrows(NullPointerException.class, () -> new FeedbackDefinition(null, "retry", "final"));
        assertThrows(NullPointerException.class, () -> new FeedbackDefinition("success", null, "final"));
        assertThrows(NullPointerException.class, () -> new FeedbackDefinition("success", "retry", null));
    }

    private ExerciseMetadata metadata() {
        return new ExerciseMetadata(
                "series",
                ExerciseType.OOP_LADDER,
                ActivityType.OOP_PRACTICE,
                InteractionType.OPEN_QUESTION,
                ValidationType.TEXT_KEYWORDS,
                ProgrammingPattern.NONE,
                SupportLevel.NOT_SPECIFIED,
                Set.of()
        );
    }

    private CaseStudy caseStudy() {
        return new CaseStudy("route", "casus", "beschrijving");
    }
}
