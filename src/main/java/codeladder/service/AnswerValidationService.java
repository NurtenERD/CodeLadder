package codeladder.service;

import codeladder.service.validation.AnswerValidatorRegistry;
import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.ValidationResult;

public class AnswerValidationService {
    private final FeedbackService feedbackService;
    private final AnswerValidatorRegistry validatorRegistry;

    public AnswerValidationService(FeedbackService feedbackService, AnswerValidatorRegistry validatorRegistry) {
        this.feedbackService = feedbackService;
        this.validatorRegistry = validatorRegistry;
    }

    public ValidationResult validate(Exercise exercise, ExerciseResponse response, int attemptNumber) {
        if (response == null || response.isEmpty()) {
            return feedbackService.createEmptyAnswerResult(attemptNumber);
        }
        return validatorRegistry.find(exercise).validate(exercise, response, attemptNumber);
    }
}
