package codeladder.service;

import codeladder.data.ExerciseDataProvider;
import codeladder.model.AttemptResult;
import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.Feedback;
import codeladder.model.FeedbackType;
import codeladder.model.StudentAnswer;
import codeladder.model.ValidationResult;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProgressServiceTest {

    @Test
    void getAllAnswersReturnsImmutableSnapshot() {
        ExerciseDataProvider provider = new ExerciseDataProvider();
        AnswerValidationService validationService = new AnswerValidationService(new FeedbackService());
        ProgressService progressService = new ProgressService();

        Exercise firstExercise = provider.getExercises().getFirst();
        Exercise secondExercise = provider.getExercises().get(1);
        progressService.recordAttempt(
                firstExercise,
                ExerciseResponse.forText("eerste antwoord"),
                validationService.validate(firstExercise, ExerciseResponse.forText("eerste antwoord"), 1)
        );
        progressService.recordAttempt(
                secondExercise,
                ExerciseResponse.forText("tweede antwoord"),
                validationService.validate(secondExercise, ExerciseResponse.forText("tweede antwoord"), 1)
        );

        Collection<StudentAnswer> snapshot = progressService.getAllAnswers();
        List<StudentAnswer> answers = new ArrayList<>(snapshot);
        assertEquals(List.of(firstExercise.getId(), secondExercise.getId()), answers.stream().map(StudentAnswer::getExerciseId).toList());
        assertThrows(UnsupportedOperationException.class, snapshot::clear);

        answers.getFirst().addAttempt(new AttemptResult(
                99,
                ExerciseResponse.forText("buiten de service"),
                new Feedback("losse feedback", FeedbackType.GUIDANCE),
                false
        ));

        assertEquals(1, progressService.getAttemptCount(firstExercise.getId()));
        assertEquals(2, answers.getFirst().getAttemptCount());
    }
}
