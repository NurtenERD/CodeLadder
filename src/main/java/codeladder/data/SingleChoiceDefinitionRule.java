package codeladder.data;

import codeladder.data.dto.ExerciseDto;
import codeladder.model.ValidationType;

import java.util.Set;

public class SingleChoiceDefinitionRule extends AbstractChoiceDefinitionRule {
    @Override
    public ValidationType supportedType() {
        return ValidationType.SINGLE_CHOICE;
    }

    @Override
    public void validate(ExerciseDto exercise, ParsedExerciseMetadata metadata, ExerciseValidationContext context) {
        Set<String> optionIds = uniqueOptionIds(exercise.getOptions(), context);
        Set<String> correctIds = uniqueTexts(exercise.getCorrectOptionIds(), "correctOptionIds", context);
        if (correctIds.size() != 1) {
            throw context.error("SINGLE_CHOICE vereist exact één correctOptionId");
        }
        requireExistingIds(correctIds, optionIds, context);
        rejectTextConfig(exercise, context);
        if (exercise.getStructuredAnswerDefinition() != null) {
            throw context.error("structuredAnswerDefinition mag alleen bij STRUCTURED_FIELDS");
        }
    }
}
