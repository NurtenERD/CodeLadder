package codeladder.controller.dashboard;

import codeladder.data.ExerciseDataProvider;
import codeladder.model.Exercise;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoutePresentationServiceTest {
    private final RoutePresentationService service = new RoutePresentationService();
    private final ExerciseDataProvider provider = new ExerciseDataProvider();

    @Test
    void programmingRouteIsNotPresentedAsGymAppTransfer() {
        Exercise exercise = findExercise("education-count-passing-07-independent");
        String guidance = service.buildRouteGuidance(exercise, exercise.getCaseStudy().getRouteBlockTitle(), 12, 88);
        String status = service.buildCurrentStepStatus(exercise);
        assertTrue(guidance.contains("programmeerleerlijn"));
        assertFalse(guidance.contains("GymApp afgerond"));
        assertTrue(status.contains("programmeerroute"));
    }

    @Test
    void libraryCaseIsPresentedAsTransfer() {
        Exercise exercise = findExercise("library-count-overdue-08-transfer");
        String guidance = service.buildRouteGuidance(exercise, exercise.getCaseStudy().getRouteBlockTitle(), 13, 88);
        String status = service.buildCurrentStepStatus(exercise);
        assertTrue(guidance.contains("Transfer actief"));
        assertTrue(status.contains("transfer"));
    }

    private Exercise findExercise(String id) {
        return provider.getExercises().stream()
                .filter(exercise -> id.equals(exercise.getId()))
                .findFirst()
                .orElseThrow();
    }
}
