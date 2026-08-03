package codeladder.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExerciseContentTest {

    @Test
    void exerciseDelegatesExistingContentGetters() {
        CaseStudy caseStudy = new CaseStudy("route", "casus", "beschrijving");
        ExerciseContent content = new ExerciseContent(
                "Titel",
                "Instructie",
                "Vraag",
                "Focus",
                "Hint",
                caseStudy
        );
        Exercise exercise = new Exercise(
                "id",
                StepType.UNDERSTAND_ASSIGNMENT,
                metadata(),
                content,
                new AnswerDefinition(
                        List.of(new AnswerOption("a", "Optie A")),
                        Set.of("a"),
                        Set.of("gebruiker"),
                        Set.of(),
                        null,
                        1
                ),
                new FeedbackDefinition("Goed", "Opnieuw", "Laatste hint"),
                null
        );

        assertSame(content, exercise.getContent());
        assertEquals("Titel", exercise.getTitle());
        assertEquals("Instructie", exercise.getInstruction());
        assertEquals("Vraag", exercise.getQuestion());
        assertEquals("Focus", exercise.getFocusText());
        assertEquals("Hint", exercise.getHintText());
        assertSame(caseStudy, exercise.getCaseStudy());
        assertTrue(exercise.getProgrammingTask().isEmpty());
        assertTrue(exercise.getStructuredAnswerDefinition().isEmpty());
    }

    @Test
    void nullFocusAndHintBecomeEmptyStrings() {
        ExerciseContent content = new ExerciseContent(
                "Titel",
                "Instructie",
                "Vraag",
                null,
                null,
                new CaseStudy("route", "casus", "beschrijving")
        );

        assertEquals("", content.getFocusText());
        assertEquals("", content.getHintText());
    }

    @Test
    void requiredValuesRejectNull() {
        CaseStudy caseStudy = new CaseStudy("route", "casus", "beschrijving");
        assertThrows(NullPointerException.class, () -> new ExerciseContent(null, "i", "q", "", "", caseStudy));
        assertThrows(NullPointerException.class, () -> new ExerciseContent("t", null, "q", "", "", caseStudy));
        assertThrows(NullPointerException.class, () -> new ExerciseContent("t", "i", null, "", "", caseStudy));
        assertThrows(NullPointerException.class, () -> new ExerciseContent("t", "i", "q", "", "", null));
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
}
