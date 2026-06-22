package codeladder.service;

import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.ExerciseType;
import codeladder.model.ValidationResult;
import codeladder.util.TextNormalizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AnswerValidationService {
    private final FeedbackService feedbackService;

    public AnswerValidationService(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    public ValidationResult validate(Exercise exercise, ExerciseResponse response, int attemptNumber) {
        if (response == null || response.isEmpty()) {
            return feedbackService.createEmptyAnswerResult(attemptNumber);
        }

        if (exercise.isCodeExercise()) {
            return validateCodeLikeTextExercise(exercise, response, attemptNumber);
        }

        return switch (exercise.getExerciseType()) {
            case OPEN_QUESTION, REFLECTION, FILL_IN_THE_BLANK -> validateTextExercise(exercise, response, attemptNumber);
            case MULTIPLE_CHOICE, CATEGORY_CHOICE -> validateSingleChoiceExercise(exercise, response, attemptNumber);
            case MULTI_SELECT, ERROR_ANALYSIS -> validateMultiSelectExercise(exercise, response, attemptNumber);
            case CODE_WRITING -> validateCodeLikeTextExercise(exercise, response, attemptNumber);
        };
    }

    private ValidationResult validateTextExercise(Exercise exercise, ExerciseResponse response, int attemptNumber) {
        int matchedKeywords = countMatchedKeywords(response.getTextAnswer(), exercise.getAcceptedKeywords());
        boolean correct = matchedKeywords >= exercise.getMinimumRequiredMatches();
        return feedbackService.createTextFeedback(exercise, response.getTextAnswer(), matchedKeywords, correct, attemptNumber);
    }

    private ValidationResult validateSingleChoiceExercise(Exercise exercise, ExerciseResponse response, int attemptNumber) {
        Set<String> selectedIds = response.getSelectedOptionIds();
        boolean correct = selectedIds.equals(exercise.getCorrectOptionIds());
        return feedbackService.createChoiceFeedback(exercise, response, correct, attemptNumber);
    }

    private ValidationResult validateMultiSelectExercise(Exercise exercise, ExerciseResponse response, int attemptNumber) {
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

    private int countMatchedKeywords(String text, Set<String> acceptedKeywords) {
        String normalizedText = TextNormalizer.normalizeText(text);
        int count = 0;
        for (String keyword : acceptedKeywords) {
            if (normalizedText.contains(TextNormalizer.normalizeText(keyword))) {
                count++;
            }
        }
        return count;
    }

    private ValidationResult validateCodeLikeTextExercise(Exercise exercise, ExerciseResponse response, int attemptNumber) {
        List<String> missingFragments = findMissingRequiredFragments(response.getTextAnswer(), exercise.getRequiredFragments());
        int matchedFragments = exercise.getRequiredFragments().size() - missingFragments.size();
        boolean correct = matchedFragments >= exercise.getMinimumRequiredMatches();
        return feedbackService.createCodeFeedback(exercise, response.getTextAnswer(), matchedFragments, correct, attemptNumber, missingFragments);
    }

    private List<String> findMissingRequiredFragments(String text, Set<String> requiredFragments) {
        String normalizedText = TextNormalizer.normalizeCode(text);
        List<String> missingFragments = new ArrayList<>();
        for (String fragment : requiredFragments) {
            if (!normalizedText.contains(TextNormalizer.normalizeCode(fragment))) {
                missingFragments.add(fragment);
            }
        }
        return missingFragments;
    }
}
