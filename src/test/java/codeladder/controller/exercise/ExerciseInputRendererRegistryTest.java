package codeladder.controller.exercise;

import codeladder.data.ExerciseConfigurationException;
import codeladder.model.Exercise;
import codeladder.model.InteractionType;
import codeladder.model.ValidationType;
import codeladder.support.ExerciseFixtures;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExerciseInputRendererRegistryTest {

    @Test
    void missingRendererGivesClearError() {
        ExerciseInputRendererRegistry registry = new ExerciseInputRendererRegistry(List.of());
        Exercise exercise = ExerciseFixtures.createExercise(
                "missing-renderer",
                InteractionType.OPEN_QUESTION,
                ValidationType.TEXT_KEYWORDS,
                List.of(),
                Set.of(),
                Set.of("gebruiker"),
                Set.of(),
                1,
                ""
        );
        ExerciseConfigurationException exception = assertThrows(
                ExerciseConfigurationException.class,
                () -> registry.render(exercise, codeladder.model.ExerciseResponse.empty())
        );
        assertTrue(exception.getMessage().contains("interactionType OPEN_QUESTION"));
        assertTrue(exception.getMessage().contains("missing-renderer"));
    }

    @Test
    void duplicateRendererRegistrationIsRejectedWithBothClassNames() {
        ExerciseConfigurationException exception = assertThrows(
                ExerciseConfigurationException.class,
                () -> new ExerciseInputRendererRegistry(List.of(new FirstRenderer(), new SecondRenderer()))
        );
        assertTrue(exception.getMessage().contains("OPEN_QUESTION"));
        assertTrue(exception.getMessage().contains("FirstRenderer"));
        assertTrue(exception.getMessage().contains("SecondRenderer"));
    }

    private static final class FirstRenderer implements ExerciseInputRenderer {
        @Override
        public Set<InteractionType> getSupportedTypes() {
            return Set.of(InteractionType.OPEN_QUESTION);
        }

        @Override
        public ExerciseInputView render(Exercise exercise, codeladder.model.ExerciseResponse initialResponse) {
            return new StubInputView();
        }
    }

    private static final class SecondRenderer implements ExerciseInputRenderer {
        @Override
        public Set<InteractionType> getSupportedTypes() {
            return Set.of(InteractionType.OPEN_QUESTION);
        }

        @Override
        public ExerciseInputView render(Exercise exercise, codeladder.model.ExerciseResponse initialResponse) {
            return new StubInputView();
        }
    }

    private static final class StubInputView implements ExerciseInputView {
        @Override
        public javafx.scene.Node getNode() {
            return new Pane();
        }

        @Override
        public codeladder.model.ExerciseResponse readResponse() {
            return codeladder.model.ExerciseResponse.empty();
        }

        @Override
        public void setDisabled(boolean disabled) {
        }
    }
}
