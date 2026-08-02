package codeladder.data;

import codeladder.model.Exercise;
import codeladder.model.LearningStep;
import codeladder.model.StepType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExerciseDataProviderTest {

    @Test
    void providesSevenStepsAndExtendedOrderedRouteBlocks() {
        ExerciseDataProvider provider = new ExerciseDataProvider();
        List<LearningStep> steps = provider.getLearningSteps();
        List<Exercise> exercises = provider.getExercises();

        assertEquals(7, steps.size());
        assertEquals(StepType.UNDERSTAND_ASSIGNMENT, steps.get(0).getStepType());
        assertEquals(StepType.OBJECTS_ATTRIBUTES_METHODS, steps.get(1).getStepType());
        assertEquals(StepType.CHOOSE_CLASSES, steps.get(2).getStepType());
        assertEquals(StepType.WRITE_SMALL_CLASSES, steps.get(3).getStepType());
        assertEquals(StepType.CONSTRUCTORS, steps.get(4).getStepType());
        assertEquals(StepType.CREATE_AND_CALL_OBJECTS, steps.get(5).getStepType());
        assertEquals(StepType.CLASS_RESPONSIBILITIES, steps.get(6).getStepType());

        assertTrue(exercises.size() >= 87);
        assertBlock(exercises.subList(0, 5), "GymApp", "GymApp-hoofdroute");
        assertBlock(exercises.subList(5, 12), "Voldoendes tellen", "Programmeerroute – tellen met een voorwaarde");
        assertBlock(exercises.subList(12, 13), "Te late boeken tellen", "Transfer – tellen met een voorwaarde");
        assertBlock(exercises.subList(13, 64), "GymApp", "GymApp-hoofdroute");
        assertBlock(exercises.subList(64, 76), "ZorgApp", "ZorgApp-transfer");
        assertBlock(exercises.subList(76, exercises.size()), "OV-app-light", "OV-app-light-transfer");
    }

    private void assertBlock(List<Exercise> exercises, String expectedCaseTitle, String expectedBlockTitle) {
        for (Exercise exercise : exercises) {
            assertEquals(expectedCaseTitle, exercise.getCaseStudy().getTitle());
            assertEquals(expectedBlockTitle, exercise.getCaseStudy().getRouteBlockTitle());
        }
    }
}
