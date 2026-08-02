package codeladder.data;

import codeladder.data.dto.ExerciseDto;
import codeladder.model.ValidationType;

import java.util.Set;

public class CodeFragmentDefinitionRule extends AbstractChoiceDefinitionRule {
    @Override
    public ValidationType supportedType() {
        return ValidationType.CODE_FRAGMENTS;
    }

    @Override
    public void validate(ExerciseDto exercise, ParsedExerciseMetadata metadata, ExerciseValidationContext context) {
        Set<String> fragments = uniqueTexts(exercise.getRequiredFragments(), "requiredFragments", context);
        if (fragments.isEmpty()) {
            throw context.error("requiredFragments mag niet leeg zijn");
        }
        validateMinimum(exercise.getMinimumRequiredMatches(), 1, fragments.size(), "requiredFragments", context);
        rejectChoiceConfig(exercise, context);
        if (!uniqueTexts(exercise.getAcceptedKeywords(), "acceptedKeywords", context).isEmpty()) {
            throw context.error("acceptedKeywords past niet bij validationType " + exercise.getMetadata().getValidationType());
        }
        if (exercise.getStructuredAnswerDefinition() != null) {
            throw context.error("structuredAnswerDefinition mag alleen bij STRUCTURED_FIELDS");
        }
    }
}
