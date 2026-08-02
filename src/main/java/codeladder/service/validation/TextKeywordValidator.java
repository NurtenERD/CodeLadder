package codeladder.service.validation;

import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.ValidationType;
import codeladder.model.ValidationResult;
import codeladder.service.FeedbackService;
import codeladder.util.TextNormalizer;

public class TextKeywordValidator implements ExerciseAnswerValidator {
    private final FeedbackService feedbackService;

    public TextKeywordValidator(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @Override
    public ValidationType supportedType() {
        return ValidationType.TEXT_KEYWORDS;
    }

    @Override
    public ValidationResult validate(Exercise exercise, ExerciseResponse response, int attemptNumber) {
        int matchedKeywords = countMatches(response.getTextAnswer(), exercise);
        boolean correct = matchedKeywords >= exercise.getMinimumRequiredMatches();
        return feedbackService.createTextFeedback(exercise, response.getTextAnswer(), matchedKeywords, correct, attemptNumber);
    }

    private int countMatches(String answerText, Exercise exercise) {
        String normalizedText = TextNormalizer.normalizeText(answerText);
        int count = 0;
        for (String keyword : exercise.getAcceptedKeywords()) {
            if (normalizedText.contains(TextNormalizer.normalizeText(keyword))) {
                count++;
            }
        }
        return count;
    }
}
