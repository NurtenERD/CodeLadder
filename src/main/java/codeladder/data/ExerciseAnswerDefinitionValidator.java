package codeladder.data;

import codeladder.data.dto.ExerciseDto;

public class ExerciseAnswerDefinitionValidator {
    private final AnswerDefinitionRuleRegistry ruleRegistry;

    public ExerciseAnswerDefinitionValidator() {
        this(new AnswerDefinitionRuleRegistry(java.util.List.of(
                new TextKeywordDefinitionRule(),
                new SingleChoiceDefinitionRule(),
                new MultiSelectDefinitionRule(),
                new CodeFragmentDefinitionRule(),
                new ReflectionDefinitionRule(),
                new StructuredFieldsDefinitionRule()
        )));
    }

    ExerciseAnswerDefinitionValidator(AnswerDefinitionRuleRegistry ruleRegistry) {
        this.ruleRegistry = ruleRegistry;
    }

    public void validate(ExerciseDto exercise, ParsedExerciseMetadata metadata, ExerciseValidationContext context) {
        ruleRegistry.find(metadata, context).validate(exercise, metadata, context);
    }
}
