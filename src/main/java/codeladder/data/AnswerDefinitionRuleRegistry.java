package codeladder.data;

import codeladder.model.ValidationType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class AnswerDefinitionRuleRegistry {
    private final Map<ValidationType, AnswerDefinitionRule> rules;

    public AnswerDefinitionRuleRegistry(List<AnswerDefinitionRule> rules) {
        if (rules == null) {
            throw new IllegalArgumentException("AnswerDefinitionRules mogen niet null zijn");
        }
        Map<ValidationType, AnswerDefinitionRule> copy = new EnumMap<>(ValidationType.class);
        for (AnswerDefinitionRule rule : rules) {
            register(copy, rule);
        }
        this.rules = Map.copyOf(copy);
    }

    public AnswerDefinitionRule find(ParsedExerciseMetadata metadata, ExerciseValidationContext context) {
        AnswerDefinitionRule rule = rules.get(metadata.validationType());
        if (rule == null) {
            throw context.error("Geen definitieregel geregistreerd voor validationType " + metadata.validationType());
        }
        return rule;
    }

    private void register(Map<ValidationType, AnswerDefinitionRule> copy, AnswerDefinitionRule rule) {
        if (rule == null) {
            throw new ExerciseConfigurationException("Null AnswerDefinitionRule in registry");
        }
        ValidationType type = rule.supportedType();
        if (type == null) {
            throw new ExerciseConfigurationException("Null ValidationType in AnswerDefinitionRule " + rule.getClass().getSimpleName());
        }
        AnswerDefinitionRule existing = copy.putIfAbsent(type, rule);
        if (existing != null) {
            throw new ExerciseConfigurationException(
                    "Dubbele AnswerDefinitionRule voor " + type + ": "
                            + existing.getClass().getSimpleName()
                            + " en "
                            + rule.getClass().getSimpleName()
            );
        }
    }
}
