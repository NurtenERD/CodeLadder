package codeladder.data;

import codeladder.data.dto.ExerciseDto;
import codeladder.model.ValidationType;

public interface AnswerDefinitionRule {
    ValidationType supportedType();
    void validate(ExerciseDto exercise, ParsedExerciseMetadata metadata, ExerciseValidationContext context);
}
