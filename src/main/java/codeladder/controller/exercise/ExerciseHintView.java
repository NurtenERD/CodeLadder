package codeladder.controller.exercise;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ExerciseHintView {
    private final Label hintLabel;
    private final VBox root;

    public ExerciseHintView() {
        this.hintLabel = new Label();
        hintLabel.setWrapText(true);
        hintLabel.setMinWidth(0);
        hintLabel.setMaxWidth(Double.MAX_VALUE);
        hintLabel.getStyleClass().add("body-text");
        this.root = new VBox(hintLabel);
        root.setFillWidth(true);
        root.setMinWidth(0);
        root.setMaxWidth(Double.MAX_VALUE);
        root.getStyleClass().add("hint-card");
        root.setVisible(false);
        root.setManaged(false);
    }

    public Parent getNode() {
        return root;
    }

    public void showHint(String hintText) {
        hintLabel.setText(hintText);
        root.setVisible(true);
        root.setManaged(true);
    }
}
