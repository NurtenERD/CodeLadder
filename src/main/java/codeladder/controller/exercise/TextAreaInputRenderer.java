package codeladder.controller.exercise;

import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.InteractionType;
import javafx.scene.control.TextArea;

import java.util.Set;

public class TextAreaInputRenderer implements ExerciseInputRenderer {

    @Override
    public Set<InteractionType> getSupportedTypes() {
        return Set.of(InteractionType.OPEN_QUESTION, InteractionType.REFLECTION);
    }

    @Override
    public ExerciseInputView render(Exercise exercise, ExerciseResponse initialResponse) {
        TextArea textArea = new TextArea(initialResponse.getTextAnswer());
        textArea.setWrapText(true);
        textArea.setPrefRowCount(4);
        textArea.setMinHeight(90);
        textArea.setPrefHeight(120);
        textArea.setMaxHeight(180);
        textArea.setMinWidth(0);
        textArea.setMaxWidth(Double.MAX_VALUE);
        return new NodeBackedExerciseInputView(
                textArea,
                () -> ExerciseResponse.forText(textArea.getText()),
                textArea::setDisable
        );
    }
}
