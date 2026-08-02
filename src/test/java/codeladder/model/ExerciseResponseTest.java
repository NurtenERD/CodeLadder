package codeladder.model;

import codeladder.support.ExerciseFixtures;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExerciseResponseTest {

    @Test
    void forFieldsUsesDefensiveCopy() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("input", "5");
        ExerciseResponse response = ExerciseResponse.forFields(values);
        values.put("input", "99");
        assertEquals("5", response.getFieldValues().get("input"));
    }

    @Test
    void isEmptyProcessesFieldValuesCorrectly() {
        assertTrue(ExerciseResponse.forFields(Map.of("input", "   ")).isEmpty());
        assertFalse(ExerciseResponse.forFields(Map.of("input", "5")).isEmpty());
    }

    @Test
    void toDisplayTextStillWorksForExistingAnswers() {
        Exercise exercise = ExerciseFixtures.createExercise(
                "choice",
                InteractionType.MULTIPLE_CHOICE,
                ValidationType.SINGLE_CHOICE,
                List.of(new AnswerOption("a", "Antwoord A"), new AnswerOption("b", "Antwoord B")),
                Set.of("a"),
                Set.of(),
                Set.of(),
                0,
                ""
        );
        assertEquals("Antwoord A", ExerciseResponse.forSelections(Set.of("a")).toDisplayText(exercise));
        assertEquals("input: 5", ExerciseResponse.forFields(Map.of("input", "5")).toDisplayText(exercise));
    }
}
