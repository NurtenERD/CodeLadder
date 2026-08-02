package codeladder.service.validation;

import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.ValidationType;
import codeladder.model.ValidationResult;
import codeladder.service.FeedbackService;
import codeladder.util.TextNormalizer;

import java.util.ArrayList;
import java.util.List;

public class CodeFragmentValidator implements ExerciseAnswerValidator {
    private final FeedbackService feedbackService;

    public CodeFragmentValidator(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @Override
    public ValidationType supportedType() {
        return ValidationType.CODE_FRAGMENTS;
    }

    @Override
    public ValidationResult validate(Exercise exercise, ExerciseResponse response, int attemptNumber) {
        List<String> missingFragments = findMissingFragments(response.getTextAnswer(), exercise);
        int matchedFragments = exercise.getRequiredFragments().size() - missingFragments.size();
        boolean correct = matchedFragments >= exercise.getMinimumRequiredMatches();
        return feedbackService.createCodeFeedback(
                exercise,
                response.getTextAnswer(),
                matchedFragments,
                correct,
                attemptNumber,
                missingFragments
        );
    }

    private List<String> findMissingFragments(String answerText, Exercise exercise) {
        String normalizedText = TextNormalizer.normalizeCode(answerText);
        List<String> missingFragments = new ArrayList<>();
        for (String fragment : exercise.getRequiredFragments()) {
            if (!normalizedText.contains(TextNormalizer.normalizeCode(fragment))) {
                missingFragments.add(fragment);
            }
        }
        return missingFragments;
    }
}
