package codeladder.controller.exercise;

import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.InteractionType;
import javafx.scene.control.TextArea;

import java.util.Set;

public class CodeInputRenderer implements ExerciseInputRenderer {

    @Override
    public Set<InteractionType> getSupportedTypes() {
        return Set.of(InteractionType.CODE_WRITING);
    }

    @Override
    public ExerciseInputView render(Exercise exercise, ExerciseResponse initialResponse) {
        TextArea textArea = new TextArea(initialResponse.getTextAnswer());
        textArea.setWrapText(true);
        textArea.setPrefRowCount(9);
        textArea.setMinHeight(180);
        textArea.setPrefHeight(240);
        textArea.setMinWidth(0);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.getStyleClass().add("code-area");
        return new NodeBackedExerciseInputView(
                textArea,
                () -> ExerciseResponse.forText(textArea.getText()),
                textArea::setDisable
        );
    }
}
