package codeladder.data;

import codeladder.model.AnswerOption;
import codeladder.model.Exercise;
import codeladder.model.ExerciseType;
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

    private static final Set<ExerciseType> FOLLOW_UP_TYPES = Set.of(
            ExerciseType.MULTIPLE_CHOICE,
            ExerciseType.MULTI_SELECT,
            ExerciseType.CATEGORY_CHOICE,
            ExerciseType.ERROR_ANALYSIS
    );

    @Test
    void eachStepStartsOpenAndThenUsesChoiceQuestions() {
        ExerciseDataProvider provider = new ExerciseDataProvider();
        Map<String, List<Exercise>> groupedExercises = provider.getExercises().stream()
                .collect(Collectors.groupingBy(
                        exercise -> exercise.getCaseStudy().getRouteBlockTitle() + "|" + exercise.getStepType().name(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        for (Map.Entry<String, List<Exercise>> entry : groupedExercises.entrySet()) {
            List<Exercise> exercises = entry.getValue();
            Exercise firstExercise = exercises.get(0);

            assertEquals(
                    ExerciseType.OPEN_QUESTION,
                    firstExercise.getExerciseType(),
                    "Eerste oefening moet open zijn voor blok/stap " + entry.getKey()
            );

            for (int index = 1; index < exercises.size(); index++) {
                Exercise exercise = exercises.get(index);
                assertTrue(
                        FOLLOW_UP_TYPES.contains(exercise.getExerciseType()),
                        "Vervolgoefening moet keuzegericht zijn voor " + exercise.getId()
                );
                assertChoiceExerciseConfiguration(exercise);
            }
        }
    }

    private void assertChoiceExerciseConfiguration(Exercise exercise) {
        Set<String> optionIds = exercise.getOptions().stream()
                .map(AnswerOption::getId)
                .collect(Collectors.toSet());

        assertFalse(optionIds.isEmpty(), "Keuzevraag mist opties: " + exercise.getId());
        assertTrue(optionIds.containsAll(exercise.getCorrectOptionIds()), "Correcte ids ontbreken in options: " + exercise.getId());
        assertTrue(exercise.getAcceptedKeywords().isEmpty(), "Keuzevraag mag geen acceptedKeywords hebben: " + exercise.getId());
        assertTrue(exercise.getRequiredFragments().isEmpty(), "Keuzevraag mag geen requiredFragments hebben: " + exercise.getId());
        assertEquals(0, exercise.getMinimumRequiredMatches(), "Keuzevraag moet minimumRequiredMatches 0 hebben: " + exercise.getId());
        assertFalse(exercise.isCodeExercise(), "Keuzevraag mag geen codeExercise zijn: " + exercise.getId());

        if (exercise.getExerciseType() == ExerciseType.MULTIPLE_CHOICE
                || exercise.getExerciseType() == ExerciseType.CATEGORY_CHOICE) {
            assertEquals(1, exercise.getCorrectOptionIds().size(), "Keuzevraag moet precies één correct antwoord hebben: " + exercise.getId());
        }

        if (exercise.getExerciseType() == ExerciseType.MULTI_SELECT
                || exercise.getExerciseType() == ExerciseType.ERROR_ANALYSIS) {
            assertFalse(exercise.getCorrectOptionIds().isEmpty(), "Keuzevraag mist correcte ids: " + exercise.getId());
        }
    }
}
