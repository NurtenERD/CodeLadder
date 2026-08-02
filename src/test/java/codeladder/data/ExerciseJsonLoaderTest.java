package codeladder.data;

import codeladder.data.dto.ExerciseIndexDto;
import codeladder.model.ActivityType;
import codeladder.model.Exercise;
import codeladder.model.ExerciseType;
import codeladder.model.ProgrammingPattern;
import codeladder.model.StepType;
import codeladder.model.SupportLevel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExerciseJsonLoaderTest {

    @Test
    void loadsIndexAndExercisesInConfiguredOrder() throws Exception {
        ExerciseIndexDto indexDto = new ObjectMapper().readValue(
                Path.of("src", "main", "resources", "exercises", "index.json").toFile(),
                ExerciseIndexDto.class
        );
        assertEquals(11, indexDto.getFiles().size());
        assertEquals("exercises/gymapp-step1.json", indexDto.getFiles().getFirst());
        assertEquals("exercises/programming-step1-count-education.json", indexDto.getFiles().get(1));
        assertEquals("exercises/programming-step1-count-library-transfer.json", indexDto.getFiles().get(2));
        assertEquals("exercises/ovapp-transfer.json", indexDto.getFiles().getLast());

        ExerciseJsonLoader loader = new ExerciseJsonLoader();
        List<Exercise> exercises = loader.loadExercises();

        assertFalse(exercises.isEmpty());
        assertEquals("GymApp-hoofdroute", exercises.getFirst().getCaseStudy().getRouteBlockTitle());
        assertEquals("OV-app-light-transfer", exercises.getLast().getCaseStudy().getRouteBlockTitle());
        assertUniqueIds(exercises);
        assertMetadataContracts(exercises);
    }

    private void assertUniqueIds(List<Exercise> exercises) {
        Set<String> ids = new HashSet<>();
        for (Exercise exercise : exercises) {
            assertTrue(ids.add(exercise.getId()), "Dubbel oefening-id gevonden: " + exercise.getId());
            assertNotNull(exercise.getMetadata());
            assertNotNull(exercise.getStepType());
        }
    }

    private void assertMetadataContracts(List<Exercise> exercises) {
        Set<StepType> stepTypes = new HashSet<>();
        for (Exercise exercise : exercises) {
            stepTypes.add(exercise.getStepType());
            if (exercise.getExerciseType() == ExerciseType.OOP_LADDER) {
                assertEquals(ActivityType.OOP_PRACTICE, exercise.getActivityType());
                assertEquals(ProgrammingPattern.NONE, exercise.getProgrammingPattern());
                assertEquals(SupportLevel.NOT_SPECIFIED, exercise.getSupportLevel());
            }
            assertNotNull(exercise.getSkillTags());
        }
        assertEquals(Set.of(StepType.values()), stepTypes);
    }
}
