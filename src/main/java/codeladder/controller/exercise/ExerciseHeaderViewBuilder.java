package codeladder.controller.exercise;

import codeladder.model.Exercise;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ExerciseHeaderViewBuilder {

    public Parent build(Exercise exercise, int currentNumber, int totalExercises) {
        Label progressLabel = label("Oefening " + currentNumber + " van " + totalExercises, "progress-badge");
        Label stepLabel = label(exercise.getStepType().getDisplayName(), "section-title");
        Label stepDescriptionLabel = label(exercise.getStepType().getDescription(), "muted-text");
        Label titleLabel = label(exercise.getTitle(), "page-title");
        Label instructionLabel = label(exercise.getInstruction(), "body-text");
        VBox headerCard = new VBox(8, progressLabel, stepLabel, stepDescriptionLabel, titleLabel, instructionLabel);
        headerCard.setFillWidth(true);
        headerCard.setMinWidth(0);
        headerCard.setMaxWidth(Double.MAX_VALUE);
        headerCard.getStyleClass().add("exercise-card");
        return headerCard;
    }

    private Label label(String text, String styleClass) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMinWidth(0);
        label.setMaxWidth(Double.MAX_VALUE);
        label.getStyleClass().add(styleClass);
        return label;
    }
}
