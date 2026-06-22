package codeladder.data;

import codeladder.data.dto.ExerciseIndexDto;
import codeladder.model.Exercise;
import codeladder.model.ExerciseType;
import codeladder.model.StepType;
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
        ObjectMapper objectMapper = new ObjectMapper();
        ExerciseIndexDto indexDto = objectMapper.readValue(
                Path.of("src", "main", "resources", "exercises", "index.json").toFile(),
                ExerciseIndexDto.class
        );
        assertEquals(9, indexDto.getFiles().size());
        assertEquals("exercises/gymapp-step1.json", indexDto.getFiles().getFirst());
        assertEquals("exercises/ovapp-transfer.json", indexDto.getFiles().getLast());

        ExerciseJsonLoader loader = new ExerciseJsonLoader();
        List<Exercise> exercises = loader.loadExercises();

        assertFalse(exercises.isEmpty());
        assertEquals("GymApp-hoofdroute", exercises.getFirst().getCaseStudy().getRouteBlockTitle());
        assertEquals("OV-app-light-transfer", exercises.getLast().getCaseStudy().getRouteBlockTitle());

        Set<String> ids = new HashSet<>();
        for (Exercise exercise : exercises) {
            assertTrue(ids.add(exercise.getId()), "Dubbel oefening-id gevonden: " + exercise.getId());
            assertNotNull(exercise.getStepType());
            assertNotNull(exercise.getExerciseType());
        }

        Set<StepType> stepTypes = new HashSet<>();
        Set<ExerciseType> exerciseTypes = new HashSet<>();
        for (Exercise exercise : exercises) {
            stepTypes.add(exercise.getStepType());
            exerciseTypes.add(exercise.getExerciseType());
        }

        assertEquals(Set.of(StepType.values()), stepTypes);
        assertTrue(exerciseTypes.contains(ExerciseType.OPEN_QUESTION));
        assertTrue(exerciseTypes.contains(ExerciseType.MULTIPLE_CHOICE));
        assertTrue(exerciseTypes.contains(ExerciseType.MULTI_SELECT));
        assertTrue(exerciseTypes.contains(ExerciseType.CATEGORY_CHOICE));
        assertTrue(exerciseTypes.contains(ExerciseType.ERROR_ANALYSIS));
        assertFalse(exerciseTypes.contains(ExerciseType.FILL_IN_THE_BLANK));
        assertFalse(exerciseTypes.contains(ExerciseType.CODE_WRITING));
        assertFalse(exerciseTypes.contains(ExerciseType.REFLECTION));
    }
}
