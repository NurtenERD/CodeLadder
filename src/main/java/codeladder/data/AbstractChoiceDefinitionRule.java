package codeladder.data;

import codeladder.data.dto.AnswerOptionDto;
import codeladder.data.dto.ExerciseDto;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

abstract class AbstractChoiceDefinitionRule implements AnswerDefinitionRule {

    protected Set<String> uniqueOptionIds(List<AnswerOptionDto> options, ExerciseValidationContext context) {
        if (options == null || options.isEmpty()) {
            throw context.error("options mag niet leeg zijn");
        }
        Set<String> optionIds = new LinkedHashSet<>();
        for (AnswerOptionDto option : options) {
            requireText(option.getId(), "option.id", context);
            requireText(option.getLabel(), "option.label", context);
            if (!optionIds.add(option.getId())) {
                throw context.invalidValue("option.id", option.getId());
            }
        }
        return optionIds;
    }

    protected Set<String> uniqueTexts(List<String> values, String fieldName, ExerciseValidationContext context) {
        Set<String> uniqueValues = new LinkedHashSet<>();
        if (values == null) {
            return uniqueValues;
        }
        for (String value : values) {
            requireText(value, fieldName, context);
            if (!uniqueValues.add(value)) {
                throw context.invalidValue(fieldName, value);
            }
        }
        return uniqueValues;
    }

    protected void rejectOptions(ExerciseDto exercise, ExerciseValidationContext context) {
        if (exercise.getOptions() != null && !exercise.getOptions().isEmpty()) {
            throw context.error("options past niet bij validationType " + exercise.getMetadata().getValidationType());
        }
    }

    protected void rejectTextConfig(ExerciseDto exercise, ExerciseValidationContext context) {
        if (!uniqueTexts(exercise.getAcceptedKeywords(), "acceptedKeywords", context).isEmpty()) {
            throw context.error("acceptedKeywords past niet bij validationType " + exercise.getMetadata().getValidationType());
        }
        if (!uniqueTexts(exercise.getRequiredFragments(), "requiredFragments", context).isEmpty()) {
            throw context.error("requiredFragments past niet bij validationType " + exercise.getMetadata().getValidationType());
        }
    }

    protected void rejectChoiceConfig(ExerciseDto exercise, ExerciseValidationContext context) {
        if (!uniqueTexts(exercise.getCorrectOptionIds(), "correctOptionIds", context).isEmpty()) {
            throw context.error("correctOptionIds past niet bij validationType " + exercise.getMetadata().getValidationType());
        }
        rejectOptions(exercise, context);
    }

    protected void requireExistingIds(Set<String> correctIds, Set<String> optionIds, ExerciseValidationContext context) {
        for (String correctId : correctIds) {
            if (!optionIds.contains(correctId)) {
                throw context.invalidValue("correctOptionIds", correctId);
            }
        }
    }

    protected void validateMinimum(int actual, int minimum, int maximum, String fieldName, ExerciseValidationContext context) {
        if (actual < minimum) {
            throw context.error("minimumRequiredMatches moet minimaal " + minimum + " zijn voor " + fieldName);
        }
        if (actual > maximum) {
            throw context.error("minimumRequiredMatches is groter dan het aantal unieke waarden in " + fieldName);
        }
    }

    protected void requireText(String value, String fieldName, ExerciseValidationContext context) {
        if (value == null || value.isBlank()) {
            throw context.missingField(fieldName);
        }
    }
}
