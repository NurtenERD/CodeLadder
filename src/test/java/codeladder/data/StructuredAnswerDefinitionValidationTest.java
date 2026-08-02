package codeladder.data;

import codeladder.data.dto.ExerciseDto;
import codeladder.data.dto.ExerciseMetadataDto;
import codeladder.data.dto.StructuredAnswerDefinitionDto;
import codeladder.data.dto.StructuredFieldDefinitionDto;
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

class StructuredAnswerDefinitionValidationTest {
    private final ExerciseDefinitionValidator validator = new ExerciseDefinitionValidator(new EnumValueParser());

    @Test
    void duplicateFieldIdsAreRejected() {
        ExerciseDto dto = analysisExercise("dup-fields");
        StructuredFieldDefinitionDto field = StructuredFieldDtoFactory.textField("same", SkillTag.GOAL.name());
        StructuredAnswerDefinitionDto definition = new StructuredAnswerDefinitionDto();
        definition.setFields(List.of(field, StructuredFieldDtoFactory.textField("same", SkillTag.INPUT.name())));
        dto.setStructuredAnswerDefinition(definition);
        assertInvalid(dto, "structuredField.id");
    }

    @Test
    void fieldWithoutSkillTagIsRejected() {
        ExerciseDto dto = analysisExercise("missing-skill");
        StructuredFieldDefinitionDto field = StructuredFieldDtoFactory.textField("goal", SkillTag.GOAL.name());
        field.setSkillTag("");
        StructuredAnswerDefinitionDto definition = new StructuredAnswerDefinitionDto();
        definition.setFields(List.of(field));
        dto.setStructuredAnswerDefinition(definition);
        assertInvalid(dto, "structuredField.skillTag");
    }

    @Test
    void requiredTextFieldWithoutKeywordGroupsIsRejected() {
        ExerciseDto dto = analysisExercise("missing-groups");
        StructuredFieldDefinitionDto field = StructuredFieldDtoFactory.textField("goal", SkillTag.GOAL.name());
        field.setAcceptedKeywordGroups(List.of());
        StructuredAnswerDefinitionDto definition = new StructuredAnswerDefinitionDto();
        definition.setFields(List.of(field));
        dto.setStructuredAnswerDefinition(definition);
        assertInvalid(dto, "acceptedKeywordGroups");
    }

    @Test
    void singleChoiceFieldWithoutExactlyOneCorrectAnswerIsRejected() {
        ExerciseDto dto = analysisExercise("single-choice");
        StructuredFieldDefinitionDto field = StructuredFieldDtoFactory.singleChoiceField("goal");
        field.setCorrectOptionIds(List.of("a", "b"));
        StructuredAnswerDefinitionDto definition = new StructuredAnswerDefinitionDto();
        definition.setFields(List.of(field));
        dto.setStructuredAnswerDefinition(definition);
        assertInvalid(dto, "SINGLE_CHOICE veld vereist exact");
    }

    @Test
    void multiSelectFieldWithoutCorrectAnswerIsRejected() {
        ExerciseDto dto = analysisExercise("multi-select");
        StructuredFieldDefinitionDto field = StructuredFieldDtoFactory.multiSelectField("goal");
        field.setCorrectOptionIds(List.of());
        StructuredAnswerDefinitionDto definition = new StructuredAnswerDefinitionDto();
        definition.setFields(List.of(field));
        dto.setStructuredAnswerDefinition(definition);
        assertInvalid(dto, "MULTI_SELECT veld vereist minimaal");
    }

    private ExerciseDto analysisExercise(String id) {
        ExerciseDto dto = ExerciseDtoFactory.validExercise(id, InteractionType.ANALYSIS_FORM, ValidationType.STRUCTURED_FIELDS);
        dto.setAcceptedKeywords(List.of());
        dto.setMinimumRequiredMatches(0);
        dto.setMetadata(programmingMetadata(id));
        return dto;
    }

    private ExerciseMetadataDto programmingMetadata(String id) {
        ExerciseMetadataDto metadata = new ExerciseMetadataDto();
        metadata.setSeriesId(id);
        metadata.setExerciseType(ExerciseType.PSEUDOCODE_TO_JAVA.name());
        metadata.setActivityType(ActivityType.ANALYSE.name());
        metadata.setInteractionType(InteractionType.ANALYSIS_FORM.name());
        metadata.setValidationType(ValidationType.STRUCTURED_FIELDS.name());
        metadata.setProgrammingPattern(ProgrammingPattern.COUNT_WITH_CONDITION.name());
        metadata.setSupportLevel(SupportLevel.CHOOSE.name());
        metadata.setSkillTags(Set.of(SkillTag.GOAL.name()));
        return metadata;
    }

    private void assertInvalid(ExerciseDto dto, String expectedText) {
        ExerciseConfigurationException exception = assertThrows(
                ExerciseConfigurationException.class,
                () -> validator.validateFile("memory.json", "Blok", ExerciseDtoFactory.caseStudy(), ExerciseTestProgrammingTaskFactory.create(), List.of(dto))
        );
        assertTrue(exception.getMessage().contains(expectedText));
    }
}
