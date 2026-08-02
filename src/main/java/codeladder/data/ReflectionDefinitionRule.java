package codeladder.data;

import codeladder.data.dto.ExerciseDto;
import codeladder.model.ValidationType;

public class ReflectionDefinitionRule extends AbstractChoiceDefinitionRule {
    @Override
    public ValidationType supportedType() {
        return ValidationType.REFLECTION;
    }

    @Override
    public void validate(ExerciseDto exercise, ParsedExerciseMetadata metadata, ExerciseValidationContext context) {
        rejectChoiceConfig(exercise, context);
        rejectTextConfig(exercise, context);
        if (exercise.getMinimumRequiredMatches() != 0) {
            throw context.error("REFLECTION mag geen minimumRequiredMatches gebruiken");
        }
        if (exercise.getStructuredAnswerDefinition() != null) {
            throw context.error("structuredAnswerDefinition mag alleen bij STRUCTURED_FIELDS");
        }
    }
}
