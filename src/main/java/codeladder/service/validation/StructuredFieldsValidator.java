package codeladder.service.validation;

import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.FieldValidationOutcome;
import codeladder.model.StructuredFieldDefinition;
import codeladder.model.StructuredFieldInputType;
import codeladder.model.ValidationDetails;
import codeladder.model.ValidationType;
import codeladder.model.ValidationResult;
import codeladder.util.StructuredFieldValueCodec;
import codeladder.util.TextNormalizer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class StructuredFieldsValidator implements ExerciseAnswerValidator {
    private final StructuredFieldsFeedbackBuilder feedbackBuilder;

    public StructuredFieldsValidator(StructuredFieldsFeedbackBuilder feedbackBuilder) {
        this.feedbackBuilder = feedbackBuilder;
    }

    @Override
    public ValidationType supportedType() {
        return ValidationType.STRUCTURED_FIELDS;
    }

    @Override
    public ValidationResult validate(Exercise exercise, ExerciseResponse response, int attemptNumber) {
        Map<String, FieldValidationOutcome> outcomes = new LinkedHashMap<>();
        boolean correct = true;
        for (StructuredFieldDefinition field : exercise.getStructuredAnswerDefinition().orElseThrow().getFields()) {
            boolean fieldCorrect = validateField(field, response.getFieldValues().get(field.getId()));
            outcomes.put(field.getId(), new FieldValidationOutcome(field.getId(), field.getSkillTag(), fieldCorrect));
            if (field.isRequired() && !fieldCorrect) {
                correct = false;
            }
        }
        ValidationDetails details = new ValidationDetails(outcomes);
        if (correct) {
            return new ValidationResult(true, false, feedbackBuilder.success(exercise), details);
        }
        return new ValidationResult(false, attemptNumber < 2, feedbackBuilder.failure(exercise, outcomes, attemptNumber), details);
    }

    private boolean validateField(StructuredFieldDefinition field, String rawValue) {
        return switch (field.getInputType()) {
            case TEXT -> validateText(field, rawValue);
            case SINGLE_CHOICE -> field.getCorrectOptionIds().contains(rawValue == null ? "" : rawValue.trim());
            case MULTI_SELECT -> StructuredFieldValueCodec.decodeMultiSelect(rawValue).equals(field.getCorrectOptionIds());
        };
    }

    private boolean validateText(StructuredFieldDefinition field, String rawValue) {
        String value = rawValue == null ? "" : rawValue.trim();
        if (field.isRequired() && value.isBlank()) {
            return false;
        }
        String normalized = TextNormalizer.normalizeText(value);
        for (Set<String> group : field.getAcceptedKeywordGroups()) {
            if (!matchesGroup(normalized, group)) {
                return false;
            }
        }
        return !field.isRequired() || !field.getAcceptedKeywordGroups().isEmpty();
    }

    private boolean matchesGroup(String normalizedAnswer, Set<String> group) {
        for (String candidate : group) {
            if (normalizedAnswer.contains(TextNormalizer.normalizeText(candidate))) {
                return true;
            }
        }
        return false;
    }
}
