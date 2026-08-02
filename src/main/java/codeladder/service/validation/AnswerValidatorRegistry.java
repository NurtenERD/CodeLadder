package codeladder.service.validation;

import codeladder.data.ExerciseConfigurationException;
import codeladder.model.Exercise;
import codeladder.model.ValidationType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class AnswerValidatorRegistry {
    private final Map<ValidationType, ExerciseAnswerValidator> validators;

    public AnswerValidatorRegistry(List<ExerciseAnswerValidator> validators) {
        this.validators = validatedCopy(validators);
    }

    public ExerciseAnswerValidator find(Exercise exercise) {
        ValidationType validationType = exercise.getValidationType();
        ExerciseAnswerValidator validator = validators.get(validationType);
        if (validator == null) {
            throw new ExerciseConfigurationException(
                    "Geen validator geregistreerd voor validationType " + validationType + " bij oefening " + exercise.getId()
            );
        }
        return validator;
    }

    private Map<ValidationType, ExerciseAnswerValidator> validatedCopy(List<ExerciseAnswerValidator> source) {
        if (source == null) {
            throw new IllegalArgumentException("Validatorregistraties mogen niet null zijn");
        }
        Map<ValidationType, ExerciseAnswerValidator> copy = new EnumMap<>(ValidationType.class);
        for (ExerciseAnswerValidator validator : source) {
            register(copy, validator);
        }
        return Map.copyOf(copy);
    }

    private void register(Map<ValidationType, ExerciseAnswerValidator> copy, ExerciseAnswerValidator validator) {
        if (validator == null) {
            throw new ExerciseConfigurationException("Null validator in AnswerValidatorRegistry");
        }
        ValidationType type = validator.supportedType();
        if (type == null) {
            throw new ExerciseConfigurationException("Null validationType in " + validator.getClass().getSimpleName());
        }
        ExerciseAnswerValidator existing = copy.putIfAbsent(type, validator);
        if (existing != null) {
            throw new ExerciseConfigurationException(
                    "Dubbele validatorregistratie voor "
                            + type
                            + ": "
                            + existing.getClass().getSimpleName()
                            + " en "
                            + validator.getClass().getSimpleName()
            );
        }
    }
}
