package codeladder.service;

import codeladder.data.ExerciseDataProvider;
import codeladder.model.ExerciseResponse;
import codeladder.model.StepType;
import codeladder.model.ValidationResult;
import codeladder.support.ValidationServiceFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LearningRouteServiceTest {

    @Test
    void routeStartsAtExerciseOneInStepOne() {
        LearningRouteService routeService = createRouteService();

        assertNotNull(routeService.getCurrentExercise());
        assertEquals(1, routeService.getCurrentExerciseNumber());
        assertEquals(StepType.UNDERSTAND_ASSIGNMENT, routeService.getCurrentExercise().getStepType());
    }

    @Test
    void moveToNextExerciseAdvancesTheRoute() {
        LearningRouteService routeService = createRouteService();

        assertTrue(routeService.moveToNextExercise());
        assertEquals(2, routeService.getCurrentExerciseNumber());
    }

    @Test
    void moveToPreviousExerciseGoesBackOneExercise() {
        LearningRouteService routeService = createRouteService();

        assertTrue(routeService.moveToNextExercise());
        assertEquals(2, routeService.getCurrentExerciseNumber());

        assertTrue(routeService.moveToPreviousExercise());
        assertEquals(1, routeService.getCurrentExerciseNumber());
    }

    @Test
    void moveToPreviousExerciseAtFirstExerciseReturnsFalse() {
        LearningRouteService routeService = createRouteService();

        assertFalse(routeService.moveToPreviousExercise());
        assertEquals(1, routeService.getCurrentExerciseNumber());
    }

    @Test
    void routeCanReachTheEndAndThenReturnsNullCurrentExercise() {
        LearningRouteService routeService = createRouteService();

        int totalExercises = routeService.getTotalExercises();
        for (int index = 1; index < totalExercises; index++) {
            assertTrue(routeService.moveToNextExercise());
        }

        assertFalse(routeService.moveToNextExercise());
        assertEquals(totalExercises + 1, routeService.getCurrentExerciseNumber());
        assertNull(routeService.getCurrentExercise());
    }

    @Test
    void restartReturnsRouteToFirstExercise() {
        LearningRouteService routeService = createRouteService();

        assertTrue(routeService.moveToNextExercise());
        routeService.restart();

        assertEquals(1, routeService.getCurrentExerciseNumber());
        assertEquals(StepType.UNDERSTAND_ASSIGNMENT, routeService.getCurrentExercise().getStepType());
    }

    @Test
    void wrongFirstAttemptAllowsSecondAttempt() {
        LearningRouteService routeService = createRouteService();

        ValidationResult validationResult = routeService.validateCurrentExercise(ExerciseResponse.forText("kort"), 1);

        assertFalse(validationResult.isCorrect());
        assertTrue(validationResult.isAllowRetry());
    }

    @Test
    void developerCanJumpToFirstExerciseOfStepThree() {
        LearningRouteService routeService = createRouteService();

        assertTrue(routeService.jumpToFirstExerciseOfStep(StepType.CHOOSE_CLASSES));
        assertNotNull(routeService.getCurrentExercise());
        assertEquals(StepType.CHOOSE_CLASSES, routeService.getCurrentExercise().getStepType());
    }

    @Test
    void developerCanJumpToEveryLearningStep() {
        LearningRouteService routeService = createRouteService();

        for (StepType stepType : StepType.values()) {
            assertTrue(routeService.jumpToFirstExerciseOfStep(stepType));
            assertNotNull(routeService.getCurrentExercise());
            assertEquals(stepType, routeService.getCurrentExercise().getStepType());
        }
    }

    @Test
    void developerCanJumpToSpecificExerciseById() {
        LearningRouteService routeService = createRouteService();
        String exerciseId = routeService.getExercises().get(0).getId();

        assertTrue(routeService.moveToNextExercise());
        assertTrue(routeService.jumpToExercise(exerciseId));
        assertNotNull(routeService.getCurrentExercise());
        assertEquals(exerciseId, routeService.getCurrentExercise().getId());
    }

    @Test
    void jumpToExerciseReturnsFalseForUnknownId() {
        LearningRouteService routeService = createRouteService();

        assertFalse(routeService.jumpToExercise("onbekende-oefening"));
    }

    @Test
    void jumpToExerciseReturnsFalseForNullId() {
        LearningRouteService routeService = createRouteService();

        assertFalse(routeService.jumpToExercise(null));
    }

    @Test
    void jumpToExerciseReturnsFalseForEmptyId() {
        LearningRouteService routeService = createRouteService();

        assertFalse(routeService.jumpToExercise(""));
    }

    private LearningRouteService createRouteService() {
        return new LearningRouteService(new ExerciseDataProvider(), ValidationServiceFactory.create());
    }
}
