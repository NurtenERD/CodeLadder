package codeladder.data;

import codeladder.model.InteractionType;
import codeladder.model.ValidationType;

import java.util.EnumMap;
import java.util.Map;

public class ExerciseCompatibilityDefinitionValidator {
    private final Map<InteractionType, ValidationType> supportedPairs;

    public ExerciseCompatibilityDefinitionValidator() {
        this.supportedPairs = createPairs();
    }

    public void validate(ParsedExerciseMetadata metadata, ExerciseValidationContext context) {
        rejectReservedTypes(metadata, context);
        ValidationType expectedType = supportedPairs.get(metadata.interactionType());
        if (expectedType == null || expectedType != metadata.validationType()) {
            throw context.error(
                    "Ongeldige combinatie interactionType/validationType: "
                            + metadata.interactionType()
                            + " / "
                            + metadata.validationType()
            );
        }
    }

    private void rejectReservedTypes(ParsedExerciseMetadata metadata, ExerciseValidationContext context) {
        if (isReserved(metadata.interactionType())) {
            throw context.error("Gereserveerd interactionType wordt nog niet ondersteund: " + metadata.interactionType());
        }
        if (isReserved(metadata.validationType())) {
            throw context.error("Gereserveerd validationType wordt nog niet ondersteund: " + metadata.validationType());
        }
    }

    private boolean isReserved(InteractionType interactionType) {
        return interactionType == InteractionType.ORDER_ITEMS;
    }

    private boolean isReserved(ValidationType validationType) {
        return validationType == ValidationType.ORDERED_ITEMS;
    }

    private Map<InteractionType, ValidationType> createPairs() {
        Map<InteractionType, ValidationType> pairs = new EnumMap<>(InteractionType.class);
        pairs.put(InteractionType.OPEN_QUESTION, ValidationType.TEXT_KEYWORDS);
        pairs.put(InteractionType.FILL_IN_THE_BLANK, ValidationType.TEXT_KEYWORDS);
        pairs.put(InteractionType.MULTIPLE_CHOICE, ValidationType.SINGLE_CHOICE);
        pairs.put(InteractionType.CATEGORY_CHOICE, ValidationType.SINGLE_CHOICE);
        pairs.put(InteractionType.MULTI_SELECT, ValidationType.MULTI_SELECT);
        pairs.put(InteractionType.ERROR_ANALYSIS, ValidationType.MULTI_SELECT);
        pairs.put(InteractionType.CODE_WRITING, ValidationType.CODE_FRAGMENTS);
        pairs.put(InteractionType.REFLECTION, ValidationType.REFLECTION);
        pairs.put(InteractionType.ANALYSIS_FORM, ValidationType.STRUCTURED_FIELDS);
        return pairs;
    }
}
