package codeladder.data;

import codeladder.data.dto.AnswerOptionDto;
import codeladder.data.dto.CaseStudyDto;
import codeladder.data.dto.ExerciseDto;
import codeladder.model.ExerciseType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class ExerciseDefinitionValidator {

    public void validateFile(String filePath, String routeBlockTitle, CaseStudyDto caseStudy, List<ExerciseDto> exercises) {
        requireNotBlank(routeBlockTitle, "routeBlockTitle", filePath);
        validateCaseStudy(caseStudy, filePath);

        if (exercises == null || exercises.isEmpty()) {
            throw new IllegalStateException("Geen oefeningen gevonden in " + filePath);
        }

        for (ExerciseDto exercise : exercises) {
            validateExercise(filePath, exercise);
        }
    }

    private void validateCaseStudy(CaseStudyDto caseStudy, String filePath) {
        if (caseStudy == null) {
            throw new IllegalStateException("caseStudy ontbreekt in " + filePath);
        }
        requireNotBlank(caseStudy.getTitle(), "caseStudy.title", filePath);
        requireNotBlank(caseStudy.getDescription(), "caseStudy.description", filePath);
    }

    private void validateExercise(String filePath, ExerciseDto exercise) {
        requireNotBlank(exercise.getId(), "id", filePath);
        requireNotBlank(exercise.getStepType(), "stepType", filePath, exercise.getId());
        requireNotBlank(exercise.getExerciseType(), "exerciseType", filePath, exercise.getId());
        requireNotBlank(exercise.getTitle(), "title", filePath, exercise.getId());
        requireNotBlank(exercise.getInstruction(), "instruction", filePath, exercise.getId());
        requireNotBlank(exercise.getQuestion(), "question", filePath, exercise.getId());
        requireNotBlank(exercise.getSuccessFeedback(), "successFeedback", filePath, exercise.getId());
        requireNotBlank(exercise.getRetryFeedback(), "retryFeedback", filePath, exercise.getId());
        requireNotBlank(exercise.getFinalFeedback(), "finalFeedback", filePath, exercise.getId());

        if (exercise.getMinimumRequiredMatches() < 0) {
            throw new IllegalStateException("minimumRequiredMatches mag niet negatief zijn in " + filePath + " voor oefening " + exercise.getId());
        }

        ExerciseType exerciseType = parseExerciseType(exercise.getExerciseType(), filePath, exercise.getId());
        validateByType(filePath, exercise, exerciseType);
    }

    private void validateByType(String filePath, ExerciseDto exercise, ExerciseType exerciseType) {
        List<AnswerOptionDto> options = exercise.getOptions() == null ? List.of() : exercise.getOptions();
        Set<String> optionIds = new HashSet<>();
        for (AnswerOptionDto option : options) {
            requireNotBlank(option.getId(), "option.id", filePath, exercise.getId());
            requireNotBlank(option.getLabel(), "option.label", filePath, exercise.getId());
            optionIds.add(option.getId());
        }

        Set<String> correctOptionIds = exercise.getCorrectOptionIds() == null ? Set.of() : exercise.getCorrectOptionIds();
        Set<String> acceptedKeywords = exercise.getAcceptedKeywords() == null ? Set.of() : exercise.getAcceptedKeywords();
        Set<String> requiredFragments = exercise.getRequiredFragments() == null ? Set.of() : exercise.getRequiredFragments();

        switch (exerciseType) {
            case MULTIPLE_CHOICE, CATEGORY_CHOICE -> {
                requireOptionsPresent(options, filePath, exercise.getId());
                if (correctOptionIds.size() != 1) {
                    throw new IllegalStateException("Keuzevraag moet precies één correctOptionId hebben in " + filePath + " voor oefening " + exercise.getId());
                }
                assertCorrectIdsExist(correctOptionIds, optionIds, filePath, exercise.getId());
            }
            case MULTI_SELECT, ERROR_ANALYSIS -> {
                requireOptionsPresent(options, filePath, exercise.getId());
                if (correctOptionIds.isEmpty()) {
                    throw new IllegalStateException("Meerkeuzevraag mist correctOptionIds in " + filePath + " voor oefening " + exercise.getId());
                }
                assertCorrectIdsExist(correctOptionIds, optionIds, filePath, exercise.getId());
            }
            case OPEN_QUESTION, FILL_IN_THE_BLANK -> {
                if (exercise.isCodeExercise()) {
                    validateCodeExercise(filePath, exercise, requiredFragments);
                } else {
                    if (acceptedKeywords.isEmpty()) {
                        throw new IllegalStateException("Tekstoefening mist acceptedKeywords in " + filePath + " voor oefening " + exercise.getId());
                    }
                    if (exercise.getMinimumRequiredMatches() < 1) {
                        throw new IllegalStateException("Tekstoefening moet minimumRequiredMatches >= 1 hebben in " + filePath + " voor oefening " + exercise.getId());
                    }
                }
            }
            case CODE_WRITING -> validateCodeExercise(filePath, exercise, requiredFragments);
            case REFLECTION -> {
                // Reflectie blijft bewust soepel.
            }
        }
    }

    private void validateCodeExercise(String filePath, ExerciseDto exercise, Set<String> requiredFragments) {
        if (!exercise.isCodeExercise()) {
            throw new IllegalStateException("Code-oefening moet codeExercise=true hebben in " + filePath + " voor oefening " + exercise.getId());
        }
        if (requiredFragments.isEmpty()) {
            throw new IllegalStateException("Code-oefening mist requiredFragments in " + filePath + " voor oefening " + exercise.getId());
        }
        if (exercise.getMinimumRequiredMatches() < 1) {
            throw new IllegalStateException("Code-oefening moet minimumRequiredMatches >= 1 hebben in " + filePath + " voor oefening " + exercise.getId());
        }
        if (exercise.getMinimumRequiredMatches() > requiredFragments.size()) {
            throw new IllegalStateException("minimumRequiredMatches is groter dan requiredFragments in " + filePath + " voor oefening " + exercise.getId());
        }
    }

    private void requireOptionsPresent(List<AnswerOptionDto> options, String filePath, String exerciseId) {
        if (options.isEmpty()) {
            throw new IllegalStateException("Options ontbreken in " + filePath + " voor oefening " + exerciseId);
        }
    }

    private void assertCorrectIdsExist(Set<String> correctOptionIds, Set<String> optionIds, String filePath, String exerciseId) {
        for (String correctId : correctOptionIds) {
            if (!optionIds.contains(correctId)) {
                throw new IllegalStateException("correctOptionId '" + correctId + "' bestaat niet in options in " + filePath + " voor oefening " + exerciseId);
            }
        }
    }

    private ExerciseType parseExerciseType(String value, String filePath, String exerciseId) {
        try {
            return ExerciseType.valueOf(value);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("Ongeldige exerciseType '" + value + "' in " + filePath + " voor oefening " + exerciseId, exception);
        }
    }

    private void requireNotBlank(String value, String field, String filePath) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(field + " ontbreekt in " + filePath);
        }
    }

    private void requireNotBlank(String value, String field, String filePath, String exerciseId) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(field + " ontbreekt in " + filePath + " voor oefening " + exerciseId);
        }
    }
}
