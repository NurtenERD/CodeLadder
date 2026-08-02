package codeladder.data;

import codeladder.data.dto.StructuredAnswerDefinitionDto;
import codeladder.data.dto.StructuredFieldDefinitionDto;
import codeladder.model.StructuredFieldInputType;
import codeladder.model.ValidationType;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class StructuredFieldsDefinitionRule extends AbstractChoiceDefinitionRule {
    @Override
    public ValidationType supportedType() {
        return ValidationType.STRUCTURED_FIELDS;
    }

    @Override
    public void validate(codeladder.data.dto.ExerciseDto exercise, ParsedExerciseMetadata metadata, ExerciseValidationContext context) {
        rejectChoiceConfig(exercise, context);
        rejectTextConfig(exercise, context);
        StructuredAnswerDefinitionDto definition = exercise.getStructuredAnswerDefinition();
        if (definition == null) {
            throw context.missingField("structuredAnswerDefinition");
        }
        if (definition.getFields() == null || definition.getFields().isEmpty()) {
            throw context.error("structuredAnswerDefinition.fields mag niet leeg zijn");
        }
        Set<String> ids = new LinkedHashSet<>();
        for (StructuredFieldDefinitionDto field : definition.getFields()) {
            validateField(field, ids, context);
        }
    }

    private void validateField(StructuredFieldDefinitionDto field, Set<String> ids, ExerciseValidationContext context) {
        requireText(field.getId(), "structuredField.id", context);
        requireText(field.getLabel(), "structuredField.label", context);
        requireText(field.getPrompt(), "structuredField.prompt", context);
        requireText(field.getInputType(), "structuredField.inputType", context);
        requireText(field.getSkillTag(), "structuredField.skillTag", context);
        requireText(field.getRetryFeedback(), "structuredField.retryFeedback", context);
        requireText(field.getFinalFeedback(), "structuredField.finalFeedback", context);
        if (!ids.add(field.getId())) {
            throw context.invalidValue("structuredField.id", field.getId());
        }
        StructuredFieldInputType inputType = parseInputType(field.getInputType(), context);
        switch (inputType) {
            case TEXT -> validateTextField(field, context);
            case SINGLE_CHOICE -> validateSingleChoiceField(field, context);
            case MULTI_SELECT -> validateMultiSelectField(field, context);
        }
    }

    private void validateTextField(StructuredFieldDefinitionDto field, ExerciseValidationContext context) {
        if (field.isRequired() && (field.getAcceptedKeywordGroups() == null || field.getAcceptedKeywordGroups().isEmpty())) {
            throw context.error("Verplicht tekstveld vereist acceptedKeywordGroups");
        }
        if (field.getOptions() != null && !field.getOptions().isEmpty()) {
            throw context.error("Tekstveld mag geen options bevatten");
        }
    }

    private void validateSingleChoiceField(StructuredFieldDefinitionDto field, ExerciseValidationContext context) {
        Set<String> optionIds = uniqueFieldOptionIds(field.getOptions(), context);
        Set<String> correctIds = uniqueTexts(field.getCorrectOptionIds(), "structuredField.correctOptionIds", context);
        if (correctIds.size() != 1) {
            throw context.error("SINGLE_CHOICE veld vereist exact één correct antwoord");
        }
        requireExistingIds(correctIds, optionIds, context);
    }

    private void validateMultiSelectField(StructuredFieldDefinitionDto field, ExerciseValidationContext context) {
        Set<String> optionIds = uniqueFieldOptionIds(field.getOptions(), context);
        Set<String> correctIds = uniqueTexts(field.getCorrectOptionIds(), "structuredField.correctOptionIds", context);
        if (correctIds.isEmpty()) {
            throw context.error("MULTI_SELECT veld vereist minimaal één correct antwoord");
        }
        requireExistingIds(correctIds, optionIds, context);
    }

    private Set<String> uniqueFieldOptionIds(List<codeladder.data.dto.AnswerOptionDto> options, ExerciseValidationContext context) {
        return uniqueOptionIds(options, context);
    }

    private StructuredFieldInputType parseInputType(String value, ExerciseValidationContext context) {
        try {
            return StructuredFieldInputType.valueOf(value);
        } catch (IllegalArgumentException exception) {
            throw context.invalidValue("structuredField.inputType", value);
        }
    }
}
