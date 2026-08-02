package codeladder.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ValidationDetails {
    private final Map<String, FieldValidationOutcome> fieldOutcomes;

    public ValidationDetails(Map<String, FieldValidationOutcome> fieldOutcomes) {
        this.fieldOutcomes = fieldOutcomes == null ? Map.of() : new LinkedHashMap<>(fieldOutcomes);
    }

    public static ValidationDetails empty() {
        return new ValidationDetails(Map.of());
    }

    public Map<String, FieldValidationOutcome> getFieldOutcomes() {
        return new LinkedHashMap<>(fieldOutcomes);
    }
}
