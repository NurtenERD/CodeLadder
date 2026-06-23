package codeladder.service;

import codeladder.data.ExerciseDataProvider;
import codeladder.model.ExerciseResponse;
import codeladder.model.StepType;
import codeladder.model.ValidationResult;
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

    private LearningRouteService createRouteService() {
        FeedbackService feedbackService = new FeedbackService();
        AnswerValidationService validationService = new AnswerValidationService(feedbackService);
        return new LearningRouteService(new ExerciseDataProvider(), validationService);
    }
}
