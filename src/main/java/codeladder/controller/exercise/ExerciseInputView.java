package codeladder.controller.exercise;

import codeladder.model.ExerciseResponse;
import javafx.scene.Node;

public interface ExerciseInputView {
    Node getNode();
    ExerciseResponse readResponse();
    void setDisabled(boolean disabled);
}
