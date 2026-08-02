package codeladder.data;

import codeladder.data.dto.ExerciseDto;
import codeladder.data.dto.ExerciseMetadataDto;
import codeladder.model.ActivityType;
import codeladder.model.ExerciseType;
import codeladder.model.InteractionType;
import codeladder.model.ProgrammingPattern;
import codeladder.model.SkillTag;
import codeladder.model.SupportLevel;
import codeladder.model.ValidationType;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExerciseJsonSchemaMigrationTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    @Test
    void existingJsonFilesUseMetadataAndNoLegacyFields() throws IOException {
        for (Path path : Files.list(Path.of("src/main/resources/exercises")).filter(file -> file.toString().endsWith(".json")).toList()) {
            if (path.endsWith("index.json")) {
                continue;
            }
            JsonNode root = OBJECT_MAPPER.readTree(path.toFile());
            for (JsonNode exercise : root.get("exercises")) {
                assertTrue(exercise.has("metadata"), "metadata ontbreekt in " + path);
                assertFalse(exercise.has("exerciseType"), "oud top-level exerciseType gevonden in " + path);
                assertFalse(exercise.has("codeExercise"), "oud codeExercise gevonden in " + path);
            }
        }
    }

    @Test
    void unknownJsonFieldsAreRejected() {
        String invalidJson = """
                {"id":"x","stepType":"UNDERSTAND_ASSIGNMENT","metadata":{"seriesId":"x","exerciseType":"OOP_LADDER","activityType":"OOP_PRACTICE","interactionType":"OPEN_QUESTION","validationType":"TEXT_KEYWORDS","programmingPattern":"NONE","supportLevel":"NOT_SPECIFIED","skillTags":[]},"title":"t","instruction":"i","question":"q","focusText":"","options":[],"correctOptionIds":[],"acceptedKeywords":["a"],"requiredFragments":[],"minimumRequiredMatches":1,"successFeedback":"s","retryFeedback":"r","finalFeedback":"f","unknown":"x"}
                """;
        assertThrows(Exception.class, () -> OBJECT_MAPPER.readValue(invalidJson, ExerciseDto.class));
    }

    @Test
    void pseudocodeToJavaWithoutProgrammingTaskIsRejected() {
        ExerciseMetadataDto metadata = new ExerciseMetadataDto();
        metadata.setSeriesId("series");
        metadata.setExerciseType(ExerciseType.PSEUDOCODE_TO_JAVA.name());
        metadata.setActivityType(ActivityType.ANALYSE.name());
        metadata.setInteractionType(InteractionType.OPEN_QUESTION.name());
        metadata.setValidationType(ValidationType.TEXT_KEYWORDS.name());
        metadata.setProgrammingPattern(ProgrammingPattern.COUNT_WITH_CONDITION.name());
        metadata.setSupportLevel(SupportLevel.CHOOSE.name());
        metadata.setSkillTags(Set.of(SkillTag.GOAL.name()));

        ExerciseDto dto = new ExerciseDto();
        dto.setId("pseudo-1");
        dto.setStepType("UNDERSTAND_ASSIGNMENT");
        dto.setMetadata(metadata);
        dto.setTitle("Titel");
        dto.setInstruction("Instructie");
        dto.setQuestion("Vraag");
        dto.setAcceptedKeywords(List.of("input"));
        dto.setMinimumRequiredMatches(1);
        dto.setSuccessFeedback("Goed");
        dto.setRetryFeedback("Opnieuw");
        dto.setFinalFeedback("Laatste hint");

        ExerciseConfigurationException exception = assertThrows(
                ExerciseConfigurationException.class,
                () -> new ExerciseDefinitionValidator(new EnumValueParser()).validateFile(
                        "memory.json",
                        "Blok",
                        new codeladder.data.dto.CaseStudyDto() {{ setTitle("Casus"); setDescription("Beschrijving"); }},
                        null,
                        List.of(dto)
                )
        );
        assertTrue(exception.getMessage().contains("PSEUDOCODE_TO_JAVA zonder ProgrammingTask"));
    }
}
