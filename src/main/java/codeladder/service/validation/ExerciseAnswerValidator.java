package codeladder.service.validation;

import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.ValidationType;
import codeladder.model.ValidationResult;

public interface ExerciseAnswerValidator {
    ValidationType supportedType();
    ValidationResult validate(Exercise exercise, ExerciseResponse response, int attemptNumber);
}
