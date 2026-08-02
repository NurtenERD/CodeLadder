package codeladder.data;

import codeladder.data.dto.AnswerOptionDto;
import codeladder.data.dto.ExerciseDto;
import codeladder.data.dto.ExerciseMetadataDto;
import codeladder.data.dto.ProgrammingTaskDto;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExerciseDefinitionValidationRulesTest {
    private final ExerciseDefinitionValidator validator = new ExerciseDefinitionValidator(new EnumValueParser());

    @Test
    void oopLadderWithoutProgrammingTaskStaysValid() {
        ExerciseDto dto = ExerciseDtoFactory.validExercise("oop-1", InteractionType.OPEN_QUESTION, ValidationType.TEXT_KEYWORDS);
        assertDoesNotThrow(() -> validate(dto));
    }

    @Test
    void textKeywordsWithoutKeywordsIsRejected() {
        ExerciseDto dto = ExerciseDtoFactory.validExercise("text-1", InteractionType.OPEN_QUESTION, ValidationType.TEXT_KEYWORDS);
        dto.setAcceptedKeywords(List.of());
        assertInvalid(dto, "acceptedKeywords");
    }

    @Test
    void textKeywordsWithMinimumZeroIsRejected() {
        ExerciseDto dto = ExerciseDtoFactory.validExercise("text-2", InteractionType.OPEN_QUESTION, ValidationType.TEXT_KEYWORDS);
        dto.setMinimumRequiredMatches(0);
        assertInvalid(dto, "minimumRequiredMatches");
    }

    @Test
    void textKeywordsWithTooHighMinimumIsRejected() {
        ExerciseDto dto = ExerciseDtoFactory.validExercise("text-3", InteractionType.OPEN_QUESTION, ValidationType.TEXT_KEYWORDS);
        dto.setAcceptedKeywords(List.of("gebruiker"));
        dto.setMinimumRequiredMatches(2);
        assertInvalid(dto, "minimumRequiredMatches");
    }

    @Test
    void singleChoiceWithZeroCorrectOptionsIsRejected() {
        ExerciseDto dto = singleChoice("single-0");
        dto.setCorrectOptionIds(List.of());
        assertInvalid(dto, "SINGLE_CHOICE");
    }

    @Test
    void singleChoiceWithMultipleCorrectOptionsIsRejected() {
        ExerciseDto dto = singleChoice("single-2");
        dto.setCorrectOptionIds(List.of("a", "b"));
        assertInvalid(dto, "SINGLE_CHOICE");
    }

    @Test
    void multiSelectWithoutCorrectOptionsIsRejected() {
        ExerciseDto dto = multiSelect("multi-0");
        dto.setCorrectOptionIds(List.of());
        assertInvalid(dto, "MULTI_SELECT");
    }

    @Test
    void codeFragmentsWithoutFragmentsIsRejected() {
        ExerciseDto dto = codeExercise("code-0");
        dto.setRequiredFragments(List.of());
        assertInvalid(dto, "requiredFragments");
    }

    @Test
    void codeFragmentsWithMinimumZeroIsRejected() {
        ExerciseDto dto = codeExercise("code-1");
        dto.setMinimumRequiredMatches(0);
        assertInvalid(dto, "minimumRequiredMatches");
    }

    @Test
    void codeFragmentsWithTooHighMinimumIsRejected() {
        ExerciseDto dto = codeExercise("code-2");
        dto.setMinimumRequiredMatches(3);
        assertInvalid(dto, "minimumRequiredMatches");
    }

    @Test
    void duplicateOptionIdsAreRejected() {
        ExerciseDto dto = singleChoice("dup-option");
        dto.setOptions(List.of(option("a"), option("a")));
        assertInvalid(dto, "option.id");
    }

    @Test
    void duplicateRequiredFragmentsAreRejected() {
        ExerciseDto dto = codeExercise("dup-fragment");
        dto.setRequiredFragments(List.of("public class X", "public class X"));
        dto.setMinimumRequiredMatches(1);
        assertInvalid(dto, "requiredFragments");
    }

    @Test
    void invalidInteractionValidationCombinationIsRejected() {
        ExerciseDto dto = ExerciseDtoFactory.validExercise("bad-combo", InteractionType.MULTIPLE_CHOICE, ValidationType.TEXT_KEYWORDS);
        dto.setOptions(List.of(option("a")));
        dto.setCorrectOptionIds(List.of("a"));
        assertInvalid(dto, "Ongeldige combinatie");
    }

    @Test
    void reservedTypesAreRejectedClearly() {
        ExerciseDto dto = ExerciseDtoFactory.validExercise("reserved", InteractionType.ORDER_ITEMS, ValidationType.ORDERED_ITEMS);
        ExerciseConfigurationException exception = assertThrows(ExerciseConfigurationException.class, () -> validate(dto));
        assertTrue(exception.getMessage().contains("Gereserveerd"));
    }

    @Test
    void pseudocodeToJavaWithoutProgrammingTaskIsRejected() {
        ExerciseDto dto = ExerciseDtoFactory.validExercise("pseudo-1", InteractionType.OPEN_QUESTION, ValidationType.TEXT_KEYWORDS);
        ExerciseMetadataDto metadata = dto.getMetadata();
        metadata.setExerciseType(ExerciseType.PSEUDOCODE_TO_JAVA.name());
        metadata.setActivityType(ActivityType.ANALYSE.name());
        metadata.setProgrammingPattern(ProgrammingPattern.COUNT_WITH_CONDITION.name());
        metadata.setSupportLevel(SupportLevel.CHOOSE.name());
        metadata.setSkillTags(Set.of(SkillTag.GOAL.name()));
        ExerciseConfigurationException exception = assertThrows(ExerciseConfigurationException.class, () -> validate(dto));
        assertTrue(exception.getMessage().contains("PSEUDOCODE_TO_JAVA zonder ProgrammingTask"));
    }

    private void validate(ExerciseDto dto) {
        validator.validateFile("memory.json", "Blok", ExerciseDtoFactory.caseStudy(), null, List.of(dto));
    }

    private void assertInvalid(ExerciseDto dto, String expectedText) {
        ExerciseConfigurationException exception = assertThrows(ExerciseConfigurationException.class, () -> validate(dto));
        assertTrue(exception.getMessage().contains(expectedText));
    }

    private ExerciseDto singleChoice(String id) {
        ExerciseDto dto = ExerciseDtoFactory.validExercise(id, InteractionType.MULTIPLE_CHOICE, ValidationType.SINGLE_CHOICE);
        dto.setOptions(List.of(option("a"), option("b")));
        dto.setCorrectOptionIds(List.of("a"));
        dto.setAcceptedKeywords(List.of());
        dto.setMinimumRequiredMatches(0);
        return dto;
    }

    private ExerciseDto multiSelect(String id) {
        ExerciseDto dto = ExerciseDtoFactory.validExercise(id, InteractionType.MULTI_SELECT, ValidationType.MULTI_SELECT);
        dto.setOptions(List.of(option("a"), option("b")));
        dto.setCorrectOptionIds(List.of("a"));
        dto.setAcceptedKeywords(List.of());
        dto.setMinimumRequiredMatches(0);
        return dto;
    }

    private ExerciseDto codeExercise(String id) {
        ExerciseDto dto = ExerciseDtoFactory.validExercise(id, InteractionType.CODE_WRITING, ValidationType.CODE_FRAGMENTS);
        dto.setAcceptedKeywords(List.of());
        dto.setRequiredFragments(List.of("public class X", "private String naam"));
        dto.setMinimumRequiredMatches(1);
        return dto;
    }

    private AnswerOptionDto option(String id) {
        AnswerOptionDto option = new AnswerOptionDto();
        option.setId(id);
        option.setLabel("Optie " + id);
        return option;
    }
}
