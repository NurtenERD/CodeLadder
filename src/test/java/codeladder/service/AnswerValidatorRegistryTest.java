package codeladder.service;

import codeladder.data.ExerciseConfigurationException;
import codeladder.model.Exercise;
import codeladder.model.InteractionType;
import codeladder.model.ValidationType;
import codeladder.model.ValidationResult;
import codeladder.service.validation.AnswerValidatorRegistry;
import codeladder.service.validation.ExerciseAnswerValidator;
import codeladder.support.ExerciseFixtures;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnswerValidatorRegistryTest {

    @Test
    void missingValidatorGivesClearError() {
        AnswerValidatorRegistry registry = new AnswerValidatorRegistry(List.of());
        Exercise exercise = ExerciseFixtures.createExercise(
                "missing-validator",
                InteractionType.OPEN_QUESTION,
                ValidationType.TEXT_KEYWORDS,
                java.util.List.of(),
                Set.of(),
                Set.of("gebruiker"),
                Set.of(),
                1,
                ""
        );
        ExerciseConfigurationException exception = assertThrows(ExerciseConfigurationException.class, () -> registry.find(exercise));
        assertTrue(exception.getMessage().contains("validationType TEXT_KEYWORDS"));
        assertTrue(exception.getMessage().contains("missing-validator"));
    }

    @Test
    void nullValidationTypeRegistrationIsRejected() {
        ExerciseConfigurationException exception = assertThrows(
                ExerciseConfigurationException.class,
                () -> new AnswerValidatorRegistry(List.of(new NullTypeValidator()))
        );

        assertTrue(exception.getMessage().contains("Null validationType"));
    }

    @Test
    void nullValidatorRegistrationIsRejected() {
        ExerciseConfigurationException exception = assertThrows(
                ExerciseConfigurationException.class,
                () -> new AnswerValidatorRegistry(java.util.Arrays.asList(new DummyValidator(), null))
        );

        assertTrue(exception.getMessage().contains("Null validator"));
    }

    @Test
    void duplicateValidatorRegistrationIsRejectedWithBothClassNames() {
        ExerciseConfigurationException exception = assertThrows(
                ExerciseConfigurationException.class,
                () -> new AnswerValidatorRegistry(List.of(new DummyValidator(), new DuplicateTextValidator()))
        );

        assertTrue(exception.getMessage().contains("TEXT_KEYWORDS"));
        assertTrue(exception.getMessage().contains("DummyValidator"));
        assertTrue(exception.getMessage().contains("DuplicateTextValidator"));
    }

    @Test
    void registryStaysImmutableAfterConstruction() {
        List<ExerciseAnswerValidator> validators = new ArrayList<>();
        validators.add(new DummyValidator());
        AnswerValidatorRegistry registry = new AnswerValidatorRegistry(validators);
        validators.clear();
        Exercise exercise = ExerciseFixtures.createExercise(
                "immutable-validator",
                InteractionType.OPEN_QUESTION,
                ValidationType.TEXT_KEYWORDS,
                java.util.List.of(),
                Set.of(),
                Set.of("gebruiker"),
                Set.of(),
                1,
                ""
        );
        assertTrue(registry.find(exercise) instanceof DummyValidator);
    }

    private static class DummyValidator implements ExerciseAnswerValidator {
        @Override
        public ValidationType supportedType() {
            return ValidationType.TEXT_KEYWORDS;
        }

        @Override
        public ValidationResult validate(Exercise exercise, codeladder.model.ExerciseResponse response, int attemptNumber) {
            throw new UnsupportedOperationException();
        }
    }

    private static final class NullTypeValidator implements ExerciseAnswerValidator {
        @Override
        public ValidationType supportedType() {
            return null;
        }

        @Override
        public ValidationResult validate(Exercise exercise, codeladder.model.ExerciseResponse response, int attemptNumber) {
            throw new UnsupportedOperationException();
        }
    }

    private static final class DuplicateTextValidator extends DummyValidator {
    }
}
