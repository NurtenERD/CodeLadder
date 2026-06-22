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
    void providesSevenStepsAndOrderedRouteBlocks() {
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

        assertTrue(exercises.size() >= 79);

        assertAllCaseTitles(exercises.subList(0, 56), "GymApp");
        assertAllCaseTitles(exercises.subList(56, 68), "ZorgApp");
        assertAllCaseTitles(exercises.subList(68, exercises.size()), "OV-app-light");
        assertAllBlockTitles(exercises.subList(0, 56), "GymApp-hoofdroute");
        assertAllBlockTitles(exercises.subList(56, 68), "ZorgApp-transfer");
        assertAllBlockTitles(exercises.subList(68, exercises.size()), "OV-app-light-transfer");
    }

    private void assertAllCaseTitles(List<Exercise> exercises, String expectedCaseTitle) {
        for (Exercise exercise : exercises) {
            assertEquals(expectedCaseTitle, exercise.getCaseStudy().getTitle());
        }
    }

    private void assertAllBlockTitles(List<Exercise> exercises, String expectedBlockTitle) {
        for (Exercise exercise : exercises) {
            assertEquals(expectedBlockTitle, exercise.getCaseStudy().getRouteBlockTitle());
        }
    }
}
