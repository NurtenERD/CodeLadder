package codeladder.controller.dashboard;

import codeladder.data.ExerciseDataProvider;
import codeladder.support.JavaFxTestSupport;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeveloperTestMenuBindingTest {

    @BeforeAll
    static void startToolkit() {
        JavaFxTestSupport.initToolkit();
    }

    @Test
    void bindNavigationCanOnlyHappenOnce() {
        DeveloperTestMenu menu = new DeveloperTestMenu(new ExerciseDataProvider().getExercises());
        menu.bindNavigation(new NoOpNavigation());
        assertThrows(IllegalStateException.class, () -> menu.bindNavigation(new NoOpNavigation()));
    }

    @Test
    void clickBeforeBindingFailsClearly() throws Exception {
        DeveloperTestMenu menu = new DeveloperTestMenu(new ExerciseDataProvider().getExercises());
        Parent view = onFxThread(menu::createView);
        Accordion accordion = (Accordion) ((VBox) view).getChildren().get(2);
        TitledPane pane = accordion.getPanes().getFirst();
        Button startButton = (Button) ((VBox) pane.getContent()).getChildren().getFirst();
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> onFxThread(() -> {
                    startButton.fire();
                    return null;
                })
        );
        assertEquals("DeveloperNavigation is nog niet gebonden", exception.getMessage());
    }

    @Test
    void boundStartStepButtonCallsNavigation() throws Exception {
        RecordingNavigation navigation = new RecordingNavigation();
        DeveloperTestMenu menu = new DeveloperTestMenu(new ExerciseDataProvider().getExercises());
        menu.bindNavigation(navigation);
        Parent view = onFxThread(menu::createView);
        Accordion accordion = (Accordion) ((VBox) view).getChildren().get(2);
        TitledPane pane = accordion.getPanes().getFirst();
        Button startButton = (Button) ((VBox) pane.getContent()).getChildren().getFirst();

        onFxThread(() -> {
            startButton.fire();
            return null;
        });

        assertEquals(codeladder.model.StepType.values()[0], navigation.startedStep);
    }

    @Test
    void boundExerciseButtonCallsNavigation() throws Exception {
        RecordingNavigation navigation = new RecordingNavigation();
        ExerciseDataProvider provider = new ExerciseDataProvider();
        DeveloperTestMenu menu = new DeveloperTestMenu(provider.getExercises());
        menu.bindNavigation(navigation);
        Parent view = onFxThread(menu::createView);
        Accordion accordion = (Accordion) ((VBox) view).getChildren().get(2);
        TitledPane pane = accordion.getPanes().getFirst();
        Button exerciseButton = (Button) ((VBox) pane.getContent()).getChildren().get(1);

        onFxThread(() -> {
            exerciseButton.fire();
            return null;
        });

        String expectedId = provider.getExercises().stream()
                .filter(exercise -> exercise.getStepType() == codeladder.model.StepType.values()[0])
                .findFirst()
                .orElseThrow()
                .getId();
        assertEquals(expectedId, navigation.startedExerciseId);
        assertTrue(navigation.startedExerciseId != null && !navigation.startedExerciseId.isBlank());
    }

    private <T> T onFxThread(java.util.concurrent.Callable<T> callable) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Object[] result = new Object[1];
        Platform.runLater(() -> {
            try {
                result[0] = callable.call();
            } catch (Throwable throwable) {
                result[0] = throwable;
            } finally {
                latch.countDown();
            }
        });
        latch.await();
        if (result[0] instanceof RuntimeException runtimeException) {
            throw runtimeException;
        }
        if (result[0] instanceof Exception exception) {
            throw exception;
        }
        @SuppressWarnings("unchecked")
        T cast = (T) result[0];
        return cast;
    }

    private static final class NoOpNavigation implements DeveloperNavigation {
        @Override
        public void startAtStep(codeladder.model.StepType stepType) {
        }

        @Override
        public void startAtExercise(String exerciseId) {
        }
    }

    private static final class RecordingNavigation implements DeveloperNavigation {
        private codeladder.model.StepType startedStep;
        private String startedExerciseId;

        @Override
        public void startAtStep(codeladder.model.StepType stepType) {
            startedStep = stepType;
        }

        @Override
        public void startAtExercise(String exerciseId) {
            startedExerciseId = exerciseId;
        }
    }
}
