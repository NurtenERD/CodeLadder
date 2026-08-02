package codeladder.data;

import codeladder.model.AnswerOption;
import codeladder.model.Exercise;
import codeladder.model.ExerciseType;
import codeladder.model.InteractionType;
import codeladder.model.ValidationType;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExerciseQuestionPatternTest {
    private static final Set<InteractionType> OOP_FOLLOW_UP_TYPES = Set.of(
            InteractionType.MULTIPLE_CHOICE,
            InteractionType.MULTI_SELECT,
            InteractionType.CATEGORY_CHOICE,
            InteractionType.ERROR_ANALYSIS
    );

    @Test
    void oopLadderStepsStartOpenAndThenUseChoiceQuestions() {
        ExerciseDataProvider provider = new ExerciseDataProvider();
        Map<String, List<Exercise>> groupedExercises = provider.getExercises().stream()
                .filter(exercise -> exercise.getExerciseType() == ExerciseType.OOP_LADDER)
                .collect(Collectors.groupingBy(
                        exercise -> exercise.getCaseStudy().getRouteBlockTitle() + "|" + exercise.getStepType().name(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
        for (Map.Entry<String, List<Exercise>> entry : groupedExercises.entrySet()) {
            List<Exercise> exercises = entry.getValue();
            assertEquals(InteractionType.OPEN_QUESTION, exercises.getFirst().getInteractionType(), "Eerste OOP-oefening moet open zijn voor " + entry.getKey());
            for (int index = 1; index < exercises.size(); index++) {
                Exercise exercise = exercises.get(index);
                assertTrue(OOP_FOLLOW_UP_TYPES.contains(exercise.getInteractionType()), "Vervolgoefening moet keuzegericht zijn voor " + exercise.getId());
                assertChoiceConfiguration(exercise);
            }
        }
    }

    private void assertChoiceConfiguration(Exercise exercise) {
        Set<String> optionIds = exercise.getOptions().stream().map(AnswerOption::getId).collect(Collectors.toSet());
        assertFalse(optionIds.isEmpty(), "Keuzevraag mist opties: " + exercise.getId());
        assertTrue(optionIds.containsAll(exercise.getCorrectOptionIds()), "Correcte ids ontbreken in options: " + exercise.getId());
        assertTrue(exercise.getAcceptedKeywords().isEmpty(), "Keuzevraag mag geen acceptedKeywords hebben: " + exercise.getId());
        assertTrue(exercise.getRequiredFragments().isEmpty(), "Keuzevraag mag geen requiredFragments hebben: " + exercise.getId());
        assertEquals(0, exercise.getMinimumRequiredMatches(), "Keuzevraag moet minimumRequiredMatches 0 hebben: " + exercise.getId());
        if (exercise.getInteractionType() == InteractionType.MULTIPLE_CHOICE || exercise.getInteractionType() == InteractionType.CATEGORY_CHOICE) {
            assertEquals(ValidationType.SINGLE_CHOICE, exercise.getValidationType());
            assertEquals(1, exercise.getCorrectOptionIds().size(), "Keuzevraag moet precies één correct antwoord hebben: " + exercise.getId());
        }
        if (exercise.getInteractionType() == InteractionType.MULTI_SELECT || exercise.getInteractionType() == InteractionType.ERROR_ANALYSIS) {
            assertEquals(ValidationType.MULTI_SELECT, exercise.getValidationType());
            assertFalse(exercise.getCorrectOptionIds().isEmpty(), "Keuzevraag mist correcte ids: " + exercise.getId());
        }
    }
}
