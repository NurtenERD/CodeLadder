package codeladder.controller.exercise;

import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.InteractionType;
import javafx.scene.control.TextField;

import java.util.Set;

public class TextFieldInputRenderer implements ExerciseInputRenderer {

    @Override
    public Set<InteractionType> getSupportedTypes() {
        return Set.of(InteractionType.FILL_IN_THE_BLANK);
    }

    @Override
    public ExerciseInputView render(Exercise exercise, ExerciseResponse initialResponse) {
        TextField textField = new TextField(initialResponse.getTextAnswer());
        textField.setMinWidth(0);
        textField.setMaxWidth(Double.MAX_VALUE);
        return new NodeBackedExerciseInputView(
                textField,
                () -> ExerciseResponse.forText(textField.getText()),
                textField::setDisable
        );
    }
}
