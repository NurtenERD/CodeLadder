package codeladder.service.validation;

import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.Feedback;
import codeladder.model.FeedbackType;
import codeladder.model.ValidationType;
import codeladder.model.ValidationResult;

public class ReflectionValidator implements ExerciseAnswerValidator {
    @Override
    public ValidationType supportedType() {
        return ValidationType.REFLECTION;
    }

    @Override
    public ValidationResult validate(Exercise exercise, ExerciseResponse response, int attemptNumber) {
        return new ValidationResult(true, false, new Feedback(exercise.getSuccessFeedback(), FeedbackType.SUCCESS));
    }
}
