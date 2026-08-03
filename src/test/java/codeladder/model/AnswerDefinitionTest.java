package codeladder.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnswerDefinitionTest {

    @Test
    void collectionsAreCopiedOrderedAndImmutable() {
        List<AnswerOption> options = new ArrayList<>();
        options.add(new AnswerOption("b", "Optie B"));
        options.add(new AnswerOption("a", "Optie A"));
        LinkedHashSet<String> correct = linkedSet("second", "first");
        LinkedHashSet<String> keywords = linkedSet("beta", "alpha");
        LinkedHashSet<String> fragments = linkedSet("frag-2", "frag-1");
        StructuredAnswerDefinition structured = new StructuredAnswerDefinition(List.of());
        AnswerDefinition definition = new AnswerDefinition(options, correct, keywords, fragments, structured, 2);
        Exercise exercise = exercise(definition);

        options.clear();
        correct.clear();
        keywords.clear();
        fragments.clear();

        assertIterableEquals(List.of("b", "a"), ids(definition.getOptions()));
        assertIterableEquals(List.of("second", "first"), new ArrayList<>(definition.getCorrectOptionIds()));
        assertIterableEquals(List.of("beta", "alpha"), new ArrayList<>(definition.getAcceptedKeywords()));
        assertIterableEquals(List.of("frag-2", "frag-1"), new ArrayList<>(definition.getRequiredFragments()));
        assertSame(definition, exercise.getAnswerDefinition());
        assertIterableEquals(List.of("b", "a"), ids(exercise.getOptions()));
        assertIterableEquals(List.of("second", "first"), new ArrayList<>(exercise.getCorrectOptionIds()));
        assertIterableEquals(List.of("beta", "alpha"), new ArrayList<>(exercise.getAcceptedKeywords()));
        assertIterableEquals(List.of("frag-2", "frag-1"), new ArrayList<>(exercise.getRequiredFragments()));
        assertTrue(exercise.getStructuredAnswerDefinition().isPresent());
        assertEquals(2, exercise.getMinimumRequiredMatches());
        assertThrows(UnsupportedOperationException.class, () -> definition.getOptions().add(new AnswerOption("x", "X")));
        assertThrows(UnsupportedOperationException.class, () -> definition.getCorrectOptionIds().add("x"));
        assertThrows(UnsupportedOperationException.class, () -> definition.getAcceptedKeywords().add("x"));
        assertThrows(UnsupportedOperationException.class, () -> definition.getRequiredFragments().add("x"));
    }

    @Test
    void nullCollectionsBecomeEmptyAndStructuredAnswerRemainsOptional() {
        AnswerDefinition definition = new AnswerDefinition(null, null, null, null, null, 0);

        assertTrue(definition.getOptions().isEmpty());
        assertTrue(definition.getCorrectOptionIds().isEmpty());
        assertTrue(definition.getAcceptedKeywords().isEmpty());
        assertTrue(definition.getRequiredFragments().isEmpty());
        assertTrue(definition.getStructuredAnswerDefinition().isEmpty());
        assertEquals(0, definition.getMinimumRequiredMatches());
    }

    private Exercise exercise(AnswerDefinition definition) {
        return new Exercise(
                "id",
                StepType.UNDERSTAND_ASSIGNMENT,
                metadata(),
                new ExerciseContent("Titel", "Instructie", "Vraag", "", "", caseStudy()),
                definition,
                new FeedbackDefinition("Goed", "Opnieuw", "Laatste hint"),
                null
        );
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

    private LinkedHashSet<String> linkedSet(String first, String second) {
        LinkedHashSet<String> values = new LinkedHashSet<>();
        values.add(first);
        values.add(second);
        return values;
    }

    private List<String> ids(List<AnswerOption> options) {
        List<String> ids = new ArrayList<>();
        for (AnswerOption option : options) {
            ids.add(option.getId());
        }
        return ids;
    }
}
