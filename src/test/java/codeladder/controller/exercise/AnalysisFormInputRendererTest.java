package codeladder.controller.exercise;

import codeladder.data.ExerciseDataProvider;
import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.support.JavaFxTestSupport;
import codeladder.util.StructuredFieldValueCodec;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnalysisFormInputRendererTest {

    @BeforeAll
    static void startToolkit() {
        JavaFxTestSupport.initToolkit();
    }

    @Test
    void readsTextFieldsCorrectly() throws Exception {
        Exercise exercise = findExercise("education-count-passing-07-independent");
        ExerciseInputView view = onFxThread(() -> new AnalysisFormInputRenderer().render(exercise, ExerciseResponse.empty()));
        TextField goal = textFields(view.getNode()).getFirst();
        onFxThread(() -> {
            goal.setText("het aantal studenten met 70 of meer");
            return null;
        });
        assertEquals("het aantal studenten met 70 of meer", view.readResponse().getFieldValues().get("goal"));
    }

    @Test
    void readsSingleChoiceFieldsCorrectly() throws Exception {
        Exercise exercise = findExercise("education-count-passing-02-input-output");
        ExerciseInputView view = onFxThread(() -> new AnalysisFormInputRenderer().render(exercise, ExerciseResponse.empty()));
        RadioButton input = radioButtons(view.getNode()).stream().filter(button -> "Een lijst met cijfers".equals(button.getText())).findFirst().orElseThrow();
        onFxThread(() -> {
            input.fire();
            return null;
        });
        assertEquals("grades", view.readResponse().getFieldValues().get("input"));
    }

    @Test
    void readsMultiSelectFieldsCorrectly() throws Exception {
        Exercise exercise = findExercise("education-count-passing-05-boundary");
        ExerciseInputView view = onFxThread(() -> new AnalysisFormInputRenderer().render(exercise, ExerciseResponse.empty()));
        List<CheckBox> boxes = checkBoxes(view.getNode());
        onFxThread(() -> {
            boxes.stream().filter(box -> "5,5".equals(box.getText()) || "5,6".equals(box.getText())).forEach(CheckBox::fire);
            return null;
        });
        assertEquals(List.of("grade-55", "grade-56"), StructuredFieldValueCodec.decodeMultiSelect(view.readResponse().getFieldValues().get("selectedGrades")).stream().toList());
    }

    @Test
    void restoresPreviousFieldValues() throws Exception {
        Exercise exercise = findExercise("education-count-passing-05-boundary");
        ExerciseResponse initial = ExerciseResponse.forFields(Map.of(
                "selectedGrades", StructuredFieldValueCodec.encodeMultiSelect(java.util.Set.of("grade-55")),
                "reason", "vanaf 5,5 telt mee"
        ));
        ExerciseInputView view = onFxThread(() -> new AnalysisFormInputRenderer().render(exercise, initial));
        assertEquals("vanaf 5,5 telt mee", textFields(view.getNode()).getFirst().getText());
        assertTrue(checkBoxes(view.getNode()).stream().anyMatch(box -> "5,5".equals(box.getText()) && box.isSelected()));
    }

    @Test
    void disablesAllFieldsAfterCompletion() throws Exception {
        Exercise exercise = findExercise("education-count-passing-02-input-output");
        ExerciseInputView view = onFxThread(() -> new AnalysisFormInputRenderer().render(exercise, ExerciseResponse.empty()));
        onFxThread(() -> {
            view.setDisabled(true);
            return null;
        });
        assertTrue(view.getNode().isDisabled());
    }

    private Exercise findExercise(String id) {
        return new ExerciseDataProvider().getExercises().stream()
                .filter(exercise -> id.equals(exercise.getId()))
                .findFirst()
                .orElseThrow();
    }

    private List<TextField> textFields(Node root) {
        return nodes(root, TextField.class);
    }

    private List<RadioButton> radioButtons(Node root) {
        return nodes(root, RadioButton.class);
    }

    private List<CheckBox> checkBoxes(Node root) {
        return nodes(root, CheckBox.class);
    }

    private <T extends Node> List<T> nodes(Node root, Class<T> type) {
        List<T> found = new ArrayList<>();
        collect(root, type, found);
        return found;
    }

    private <T extends Node> void collect(Node node, Class<T> type, List<T> found) {
        if (type.isInstance(node)) {
            found.add(type.cast(node));
        }
        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                collect(child, type, found);
            }
        }
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
}
