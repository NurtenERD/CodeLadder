package codeladder.data;

import codeladder.model.Exercise;
import codeladder.model.StepType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GymAppRouteTest {

    @Test
    void gymAppMainRouteStaysInOneContextAndProgressesByStep() {
        ExerciseDataProvider provider = new ExerciseDataProvider();
        List<Exercise> exercises = provider.getExercises().subList(0, 56);

        exercises.forEach(exercise -> assertEquals("GymApp", exercise.getCaseStudy().getTitle()));

        assertStepBlock(exercises.subList(0, 5), StepType.UNDERSTAND_ASSIGNMENT);
        assertStepBlock(exercises.subList(5, 13), StepType.OBJECTS_ATTRIBUTES_METHODS);
        assertStepBlock(exercises.subList(13, 20), StepType.CHOOSE_CLASSES);
        assertStepBlock(exercises.subList(20, 28), StepType.WRITE_SMALL_CLASSES);
        assertStepBlock(exercises.subList(28, 36), StepType.CONSTRUCTORS);
        assertStepBlock(exercises.subList(36, 46), StepType.CREATE_AND_CALL_OBJECTS);
        assertStepBlock(exercises.subList(46, 56), StepType.CLASS_RESPONSIBILITIES);
    }

    private void assertStepBlock(List<Exercise> exercises, StepType expectedStepType) {
        for (Exercise exercise : exercises) {
            assertEquals(expectedStepType, exercise.getStepType());
        }
    }
}
