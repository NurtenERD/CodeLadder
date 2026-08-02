package codeladder.data;

import codeladder.data.dto.ExerciseDto;
import codeladder.model.ValidationType;

import java.util.Set;

public class MultiSelectDefinitionRule extends AbstractChoiceDefinitionRule {
    @Override
    public ValidationType supportedType() {
        return ValidationType.MULTI_SELECT;
    }

    @Override
    public void validate(ExerciseDto exercise, ParsedExerciseMetadata metadata, ExerciseValidationContext context) {
        Set<String> optionIds = uniqueOptionIds(exercise.getOptions(), context);
        Set<String> correctIds = uniqueTexts(exercise.getCorrectOptionIds(), "correctOptionIds", context);
        if (correctIds.isEmpty()) {
            throw context.error("MULTI_SELECT vereist minimaal één correctOptionId");
        }
        requireExistingIds(correctIds, optionIds, context);
        rejectTextConfig(exercise, context);
        if (exercise.getStructuredAnswerDefinition() != null) {
            throw context.error("structuredAnswerDefinition mag alleen bij STRUCTURED_FIELDS");
        }
    }
}
