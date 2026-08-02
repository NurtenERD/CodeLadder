package codeladder.data;

import codeladder.data.dto.ExerciseDto;
import codeladder.model.ValidationType;

import java.util.Set;

public class TextKeywordDefinitionRule extends AbstractChoiceDefinitionRule {
    @Override
    public ValidationType supportedType() {
        return ValidationType.TEXT_KEYWORDS;
    }

    @Override
    public void validate(ExerciseDto exercise, ParsedExerciseMetadata metadata, ExerciseValidationContext context) {
        Set<String> keywords = uniqueTexts(exercise.getAcceptedKeywords(), "acceptedKeywords", context);
        if (keywords.isEmpty()) {
            throw context.error("acceptedKeywords mag niet leeg zijn");
        }
        validateMinimum(exercise.getMinimumRequiredMatches(), 1, keywords.size(), "acceptedKeywords", context);
        rejectChoiceConfig(exercise, context);
        if (exercise.getStructuredAnswerDefinition() != null) {
            throw context.error("structuredAnswerDefinition mag alleen bij STRUCTURED_FIELDS");
        }
    }
}
