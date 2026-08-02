package codeladder.data;

import codeladder.data.dto.ExerciseIndexDto;
import codeladder.model.ActivityType;
import codeladder.model.Exercise;
import codeladder.model.ExerciseType;
import codeladder.model.ProgrammingPattern;
import codeladder.model.StepType;
import codeladder.model.SupportLevel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProgrammingRouteJsonTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    void newProgrammingFilesArePlacedDirectlyAfterGymAppStepOne() throws Exception {
        ExerciseIndexDto index = OBJECT_MAPPER.readValue(Path.of("src/main/resources/exercises/index.json").toFile(), ExerciseIndexDto.class);
        assertEquals("exercises/gymapp-step1.json", index.getFiles().get(0));
        assertEquals("exercises/programming-step1-count-education.json", index.getFiles().get(1));
        assertEquals("exercises/programming-step1-count-library-transfer.json", index.getFiles().get(2));
        assertEquals("exercises/gymapp-step2.json", index.getFiles().get(3));
    }

    @Test
    void programmingRouteExercisesShareMetadataAndSupportSequence() {
        List<Exercise> exercises = new ExerciseDataProvider().getExercises().stream()
                .filter(exercise -> exercise.getExerciseType() == ExerciseType.PSEUDOCODE_TO_JAVA)
                .toList();
        assertEquals(8, exercises.size());
        assertEquals(
                List.of(
                        SupportLevel.CHOOSE,
                        SupportLevel.CHOOSE,
                        SupportLevel.ORDER,
                        SupportLevel.COMPLETE,
                        SupportLevel.PARTIAL_WRITE,
                        SupportLevel.PARTIAL_WRITE,
                        SupportLevel.INDEPENDENT,
                        SupportLevel.INDEPENDENT
                ),
                exercises.stream().map(Exercise::getSupportLevel).toList()
        );
        for (Exercise exercise : exercises) {
            assertEquals("count-with-condition-step1-01", exercise.getMetadata().getSeriesId());
            assertEquals(ActivityType.ANALYSE, exercise.getActivityType());
            assertEquals(ProgrammingPattern.COUNT_WITH_CONDITION, exercise.getProgrammingPattern());
            assertEquals(StepType.UNDERSTAND_ASSIGNMENT, exercise.getStepType());
        }
        Set<String> ids = new LinkedHashSet<>();
        exercises.forEach(exercise -> assertTrue(ids.add(exercise.getId())));
    }

    @Test
    void programmingTaskIsSharedPerFileAndNotDuplicatedInsideExercisesJson() throws Exception {
        List<Exercise> exercises = new ExerciseDataProvider().getExercises().stream()
                .filter(exercise -> "count-with-condition-step1-01".equals(exercise.getMetadata().getSeriesId()))
                .toList();
        assertSame(exercises.getFirst().getProgrammingTask().orElseThrow(), exercises.get(6).getProgrammingTask().orElseThrow());

        JsonNode educationFile = OBJECT_MAPPER.readTree(Path.of("src/main/resources/exercises/programming-step1-count-education.json").toFile());
        assertTrue(educationFile.has("programmingTask"));
        for (JsonNode exercise : educationFile.get("exercises")) {
            assertFalse(exercise.has("programmingTask"));
        }
    }
}
