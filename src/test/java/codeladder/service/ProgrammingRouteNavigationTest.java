package codeladder.service;

import codeladder.data.ExerciseDataProvider;
import codeladder.model.Exercise;
import codeladder.support.ValidationServiceFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProgrammingRouteNavigationTest {

    @Test
    void startAtNewProgrammingExercisesWorks() {
        LearningRouteService service = new LearningRouteService(new ExerciseDataProvider(), ValidationServiceFactory.create());
        assertTrue(service.jumpToExercise("education-count-passing-01-goal"));
        assertEquals("education-count-passing-01-goal", service.getCurrentExercise().getId());
        assertTrue(service.jumpToExercise("education-count-passing-07-independent"));
        assertEquals("education-count-passing-07-independent", service.getCurrentExercise().getId());
        assertTrue(service.jumpToExercise("library-count-overdue-08-transfer"));
        assertEquals("library-count-overdue-08-transfer", service.getCurrentExercise().getId());
    }

    @Test
    void backNavigationWithinProgrammingSeriesWorks() {
        LearningRouteService service = new LearningRouteService(new ExerciseDataProvider(), ValidationServiceFactory.create());
        assertTrue(service.jumpToExercise("library-count-overdue-08-transfer"));
        assertTrue(service.moveToPreviousExercise());
        assertEquals("education-count-passing-07-independent", service.getCurrentExercise().getId());
    }

    @Test
    void developerStepContainsEightProgrammingExercisesInStepOne() {
        List<Exercise> exercises = new ExerciseDataProvider().getExercises().stream()
                .filter(exercise -> "count-with-condition-step1-01".equals(exercise.getMetadata().getSeriesId()))
                .toList();
        assertEquals(8, exercises.size());
    }
}
