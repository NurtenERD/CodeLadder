package codeladder.data;

import codeladder.data.dto.ExerciseDto;
import codeladder.model.InteractionType;
import codeladder.model.ValidationType;
import codeladder.support.ExerciseDtoFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExerciseCompatibilityDefinitionValidatorTest {
    private final ExerciseDefinitionValidator validator = new ExerciseDefinitionValidator(new EnumValueParser());

    @Test
    void analysisFormWithOtherValidationTypeIsRejected() {
        ExerciseDto dto = ExerciseDtoFactory.validExercise("analysis-wrong", InteractionType.ANALYSIS_FORM, ValidationType.TEXT_KEYWORDS);
        ExerciseConfigurationException exception = assertThrows(
                ExerciseConfigurationException.class,
                () -> validator.validateFile("memory.json", "Blok", ExerciseDtoFactory.caseStudy(), ExerciseTestProgrammingTaskFactory.create(), List.of(dto))
        );
        assertTrue(exception.getMessage().contains("Ongeldige combinatie"));
    }

    @Test
    void structuredFieldsWithOtherInteractionTypeIsRejected() {
        ExerciseDto dto = ExerciseDtoFactory.validExercise("structured-wrong", InteractionType.MULTIPLE_CHOICE, ValidationType.STRUCTURED_FIELDS);
        ExerciseConfigurationException exception = assertThrows(
                ExerciseConfigurationException.class,
                () -> validator.validateFile("memory.json", "Blok", ExerciseDtoFactory.caseStudy(), ExerciseTestProgrammingTaskFactory.create(), List.of(dto))
        );
        assertTrue(exception.getMessage().contains("Ongeldige combinatie"));
    }
}
