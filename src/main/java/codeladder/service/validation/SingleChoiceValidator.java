package codeladder.service.validation;

import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.ValidationType;
import codeladder.model.ValidationResult;
import codeladder.service.FeedbackService;

public class SingleChoiceValidator implements ExerciseAnswerValidator {
    private final FeedbackService feedbackService;

    public SingleChoiceValidator(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @Override
    public ValidationType supportedType() {
        return ValidationType.SINGLE_CHOICE;
    }

    @Override
    public ValidationResult validate(Exercise exercise, ExerciseResponse response, int attemptNumber) {
        boolean correct = response.getSelectedOptionIds().equals(exercise.getCorrectOptionIds());
        return feedbackService.createChoiceFeedback(exercise, response, correct, attemptNumber);
    }
}
