package codeladder.service.validation;

import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.ValidationType;
import codeladder.model.ValidationResult;
import codeladder.service.FeedbackService;

import java.util.Set;

public class MultiSelectValidator implements ExerciseAnswerValidator {
    private final FeedbackService feedbackService;

    public MultiSelectValidator(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @Override
    public ValidationType supportedType() {
        return ValidationType.MULTI_SELECT;
    }

    @Override
    public ValidationResult validate(Exercise exercise, ExerciseResponse response, int attemptNumber) {
        Set<String> selectedIds = response.getSelectedOptionIds();
        Set<String> correctIds = exercise.getCorrectOptionIds();
        boolean correct = selectedIds.equals(correctIds);
        int overlap = 0;
        for (String selectedId : selectedIds) {
            if (correctIds.contains(selectedId)) {
                overlap++;
            }
        }
        return feedbackService.createSelectionFeedback(exercise, response, correct, overlap, attemptNumber);
    }
}
