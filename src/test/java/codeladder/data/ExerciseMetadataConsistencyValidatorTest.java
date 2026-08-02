package codeladder.data;

import codeladder.data.dto.ExerciseDto;
import codeladder.data.dto.ExerciseMetadataDto;
import codeladder.data.dto.ProgrammingTaskDto;
import codeladder.data.dto.ProgrammingTestCaseDto;
import codeladder.model.ActivityType;
import codeladder.model.ExerciseType;
import codeladder.model.InteractionType;
import codeladder.model.ProgrammingPattern;
import codeladder.model.SkillTag;
import codeladder.model.SupportLevel;
import codeladder.model.ValidationType;
import codeladder.support.ExerciseDtoFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExerciseMetadataConsistencyValidatorTest {
    private final ExerciseDefinitionValidator validator = new ExerciseDefinitionValidator(new EnumValueParser());

    @Test
    void oopLadderWithWrongActivityTypeIsRejected() {
        ExerciseDto dto = ExerciseDtoFactory.validExercise("oop-activity", InteractionType.OPEN_QUESTION, ValidationType.TEXT_KEYWORDS);
        dto.getMetadata().setActivityType(ActivityType.ANALYSE.name());
        assertInvalid(dto, null, "OOP_LADDER vereist ActivityType.OOP_PRACTICE");
    }

    @Test
    void oopLadderWithWrongProgrammingPatternIsRejected() {
        ExerciseDto dto = ExerciseDtoFactory.validExercise("oop-pattern", InteractionType.OPEN_QUESTION, ValidationType.TEXT_KEYWORDS);
        dto.getMetadata().setProgrammingPattern(ProgrammingPattern.COUNT_WITH_CONDITION.name());
        assertInvalid(dto, null, "OOP_LADDER vereist ProgrammingPattern.NONE");
    }

    @Test
    void pseudocodeToJavaWithOopPracticeIsRejected() {
        ExerciseDto dto = programmingExercise("pseudo-activity", InteractionType.OPEN_QUESTION, ValidationType.TEXT_KEYWORDS);
        dto.getMetadata().setActivityType(ActivityType.OOP_PRACTICE.name());
        assertInvalid(dto, programmingTask(), "PSEUDOCODE_TO_JAVA mag geen OOP_PRACTICE gebruiken");
    }

    @Test
    void pseudocodeToJavaWithPatternNoneIsRejected() {
        ExerciseDto dto = programmingExercise("pseudo-pattern", InteractionType.OPEN_QUESTION, ValidationType.TEXT_KEYWORDS);
        dto.getMetadata().setProgrammingPattern(ProgrammingPattern.NONE.name());
        assertInvalid(dto, programmingTask(), "PSEUDOCODE_TO_JAVA vereist een programmeerpatroon");
    }

    @Test
    void pseudocodeToJavaWithNotSpecifiedSupportLevelIsRejected() {
        ExerciseDto dto = programmingExercise("pseudo-support", InteractionType.OPEN_QUESTION, ValidationType.TEXT_KEYWORDS);
        dto.getMetadata().setSupportLevel(SupportLevel.NOT_SPECIFIED.name());
        assertInvalid(dto, programmingTask(), "PSEUDOCODE_TO_JAVA vereist een supportLevel");
    }

    @Test
    void pseudocodeToJavaWithoutSkillTagIsRejected() {
        ExerciseDto dto = programmingExercise("pseudo-skills", InteractionType.OPEN_QUESTION, ValidationType.TEXT_KEYWORDS);
        dto.getMetadata().setSkillTags(Set.of());
        assertInvalid(dto, programmingTask(), "PSEUDOCODE_TO_JAVA vereist minimaal");
    }

    @Test
    void transferWithoutIndependentSupportLevelIsRejected() {
        ExerciseDto dto = programmingExercise("transfer-support", InteractionType.ANALYSIS_FORM, ValidationType.STRUCTURED_FIELDS);
        dto.getMetadata().setSupportLevel(SupportLevel.PARTIAL_WRITE.name());
        dto.getMetadata().setSkillTags(Set.of(SkillTag.GOAL.name(), SkillTag.TRANSFER.name()));
        dto.setStructuredAnswerDefinition(StructuredFieldDtoFactory.analysisForm("goal"));
        assertInvalid(dto, programmingTask(), "TRANSFER vereist SupportLevel.INDEPENDENT");
    }

    private ExerciseDto programmingExercise(String id, InteractionType interactionType, ValidationType validationType) {
        ExerciseDto dto = ExerciseDtoFactory.validExercise(id, interactionType, validationType);
        ExerciseMetadataDto metadata = dto.getMetadata();
        metadata.setExerciseType(ExerciseType.PSEUDOCODE_TO_JAVA.name());
        metadata.setActivityType(ActivityType.ANALYSE.name());
        metadata.setProgrammingPattern(ProgrammingPattern.COUNT_WITH_CONDITION.name());
        metadata.setSupportLevel(SupportLevel.CHOOSE.name());
        metadata.setSkillTags(Set.of(SkillTag.GOAL.name()));
        return dto;
    }

    private void assertInvalid(ExerciseDto dto, ProgrammingTaskDto programmingTask, String expectedText) {
        ExerciseConfigurationException exception = assertThrows(
                ExerciseConfigurationException.class,
                () -> validator.validateFile("memory.json", "Blok", ExerciseDtoFactory.caseStudy(), programmingTask, List.of(dto))
        );
        assertTrue(exception.getMessage().contains(expectedText));
    }

    private ProgrammingTaskDto programmingTask() {
        ProgrammingTaskDto dto = new ProgrammingTaskDto();
        dto.setInputDescription("Lijst");
        dto.setOutputDescription("Aantal");
        dto.setRules(List.of("regel"));
        dto.setEdgeCases(List.of("leeg"));
        dto.setPseudocodeSteps(List.of());
        dto.setJavaTemplate("");
        ProgrammingTestCaseDto testCase = new ProgrammingTestCaseDto();
        testCase.setDescription("voorbeeld");
        testCase.setInput("[]");
        testCase.setExpectedOutput("0");
        dto.setTestCases(List.of(testCase));
        dto.setExplanationKeywords(Set.of("lijst"));
        return dto;
    }
}
