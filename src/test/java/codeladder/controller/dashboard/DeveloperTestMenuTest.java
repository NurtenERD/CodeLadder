package codeladder.controller.dashboard;

import codeladder.data.ExerciseDataProvider;
import codeladder.model.Exercise;
import codeladder.model.StepType;
import codeladder.support.JavaFxTestSupport;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeveloperTestMenuTest {

    @BeforeAll
    static void startToolkit() {
        JavaFxTestSupport.initToolkit();
    }

    @Test
    void developerMenuReceivesAllStepsExercisesAndRouteOrder() throws Exception {
        ExerciseDataProvider provider = new ExerciseDataProvider();
        Parent view = onFxThread(() -> {
            DeveloperTestMenu menu = new DeveloperTestMenu(provider.getExercises());
            menu.bindNavigation(new NoOpNavigation());
            return menu.createView();
        });
        VBox container = (VBox) view;
        Accordion accordion = (Accordion) container.getChildren().get(2);
        assertEquals(StepType.values().length, accordion.getPanes().size());

        List<String> exerciseButtonTexts = new ArrayList<>();
        List<String> expected = new ArrayList<>();
        for (int paneIndex = 0; paneIndex < accordion.getPanes().size(); paneIndex++) {
            TitledPane pane = accordion.getPanes().get(paneIndex);
            VBox contentBox = (VBox) pane.getContent();
            for (int index = 1; index < contentBox.getChildren().size(); index++) {
                exerciseButtonTexts.add(((Button) contentBox.getChildren().get(index)).getText());
            }
            StepType stepType = StepType.values()[paneIndex];
            provider.getExercises().stream()
                    .filter(exercise -> exercise.getStepType() == stepType)
                    .map(this::displayText)
                    .forEach(expected::add);
        }
        assertEquals(expected, exerciseButtonTexts);
    }

    private String displayText(Exercise exercise) {
        return exercise.getCaseStudy().getTitle() + " - " + exercise.getTitle();
    }

    private <T> T onFxThread(java.util.concurrent.Callable<T> callable) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Object[] result = new Object[1];
        Platform.runLater(() -> {
            try {
                result[0] = callable.call();
            } catch (Exception exception) {
                result[0] = exception;
            } finally {
                latch.countDown();
            }
        });
        latch.await();
        if (result[0] instanceof Exception exception) {
            throw exception;
        }
        @SuppressWarnings("unchecked")
        T cast = (T) result[0];
        return cast;
    }

    private static final class NoOpNavigation implements DeveloperNavigation {
        @Override
        public void startAtStep(StepType stepType) {
        }

        @Override
        public void startAtExercise(String exerciseId) {
        }
    }
}
