package codeladder.model;

import codeladder.util.StructuredFieldValueCodec;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

public class ExerciseResponse {
    private final String textAnswer;
    private final Set<String> selectedOptionIds;
    private final Map<String, String> fieldValues;

    private ExerciseResponse(String textAnswer, Set<String> selectedOptionIds, Map<String, String> fieldValues) {
        this.textAnswer = textAnswer == null ? "" : textAnswer.trim();
        this.selectedOptionIds = selectedOptionIds == null ? Set.of() : new LinkedHashSet<>(selectedOptionIds);
        this.fieldValues = copyFields(fieldValues);
    }

    public static ExerciseResponse empty() {
        return new ExerciseResponse("", Set.of(), Map.of());
    }

    public static ExerciseResponse forText(String textAnswer) {
        return new ExerciseResponse(textAnswer, Set.of(), Map.of());
    }

    public static ExerciseResponse forSelections(Set<String> selectedOptionIds) {
        return new ExerciseResponse("", selectedOptionIds, Map.of());
    }

    public static ExerciseResponse forFields(Map<String, String> fieldValues) {
        return new ExerciseResponse("", Set.of(), fieldValues);
    }

    public String getTextAnswer() {
        return textAnswer;
    }

    public Set<String> getSelectedOptionIds() {
        return new LinkedHashSet<>(selectedOptionIds);
    }

    public Map<String, String> getFieldValues() {
        return new LinkedHashMap<>(fieldValues);
    }

    public boolean isEmpty() {
        return textAnswer.isBlank() && selectedOptionIds.isEmpty() && fieldValues.values().stream().allMatch(String::isBlank);
    }

    public String toDisplayText(Exercise exercise) {
        if (!textAnswer.isBlank()) {
            return textAnswer;
        }
        if (!selectedOptionIds.isEmpty()) {
            return selectedLabels(exercise);
        }
        if (!fieldValues.isEmpty()) {
            return displayFields(exercise);
        }
        return "(geen antwoord)";
    }

    private String selectedLabels(Exercise exercise) {
        StringJoiner joiner = new StringJoiner(", ");
        for (AnswerOption option : exercise.getOptions()) {
            if (selectedOptionIds.contains(option.getId())) {
                joiner.add(option.getLabel());
            }
        }
        return joiner.toString();
    }

    private String displayFields(Exercise exercise) {
        StringJoiner joiner = new StringJoiner(", ");
        for (Map.Entry<String, String> entry : fieldValues.entrySet()) {
            joiner.add(displayField(exercise, entry.getKey(), entry.getValue()));
        }
        return joiner.toString();
    }

    private String displayField(Exercise exercise, String fieldId, String rawValue) {
        if (exercise.getStructuredAnswerDefinition().isEmpty()) {
            return fieldId + ": " + rawValue;
        }
        for (StructuredFieldDefinition field : exercise.getStructuredAnswerDefinition().orElseThrow().getFields()) {
            if (field.getId().equals(fieldId)) {
                return field.getLabel() + ": " + displayFieldValue(field, rawValue);
            }
        }
        return fieldId + ": " + rawValue;
    }

    private String displayFieldValue(StructuredFieldDefinition field, String rawValue) {
        return switch (field.getInputType()) {
            case TEXT -> rawValue;
            case SINGLE_CHOICE -> optionLabel(field, rawValue);
            case MULTI_SELECT -> optionLabels(field, rawValue);
        };
    }

    private String optionLabel(StructuredFieldDefinition field, String optionId) {
        for (AnswerOption option : field.getOptions()) {
            if (option.getId().equals(optionId)) {
                return option.getLabel();
            }
        }
        return optionId;
    }

    private String optionLabels(StructuredFieldDefinition field, String rawValue) {
        StringJoiner joiner = new StringJoiner(", ");
        Set<String> selectedIds = StructuredFieldValueCodec.decodeMultiSelect(rawValue);
        for (AnswerOption option : field.getOptions()) {
            if (selectedIds.contains(option.getId())) {
                joiner.add(option.getLabel());
            }
        }
        return joiner.toString();
    }

    private Map<String, String> copyFields(Map<String, String> values) {
        Map<String, String> copy = new LinkedHashMap<>();
        if (values == null) {
            return copy;
        }
        for (Map.Entry<String, String> entry : values.entrySet()) {
            copy.put(entry.getKey(), entry.getValue() == null ? "" : entry.getValue().trim());
        }
        return copy;
    }
}
