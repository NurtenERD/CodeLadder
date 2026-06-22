package codeladder.controller;

import codeladder.model.SummaryItem;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Consumer;

public class SummaryScreenController {

    public Parent createView(
            List<SummaryItem> summaryItems,
            String currentReflection,
            Consumer<String> onSaveReflection,
            Runnable onRestart
    ) {
        Label titleLabel = new Label("Eindoverzicht");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label introLabel = new Label("Hier zie je welke oefeningen je hebt gemaakt, wat je hebt ingevuld en welke feedback je kreeg.");
        introLabel.setWrapText(true);

        VBox summaryBox = new VBox(14);
        for (SummaryItem item : summaryItems) {
            Label stepLabel = new Label(item.getStepTitle());
            stepLabel.setStyle("-fx-font-weight: bold;");

            Label exerciseLabel = new Label(item.getExerciseTitle());
            exerciseLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            Label answerLabel = new Label("Antwoord: " + item.getAnswerText());
            answerLabel.setWrapText(true);

            Label feedbackLabel = new Label("Feedback: " + item.getFeedbackText());
            feedbackLabel.setWrapText(true);

            Label attemptLabel = new Label("Pogingen: " + item.getAttemptCount());

            VBox itemBox = new VBox(4, stepLabel, exerciseLabel, answerLabel, feedbackLabel, attemptLabel);
            summaryBox.getChildren().add(itemBox);
        }

        Label reflectionLabel = new Label("Wat heb je geleerd over classes, constructors, objecten en verantwoordelijkheden?");
        reflectionLabel.setStyle("-fx-font-weight: bold;");

        TextArea reflectionArea = new TextArea(currentReflection);
        reflectionArea.setWrapText(true);
        reflectionArea.setPrefRowCount(4);

        Button saveReflectionButton = new Button("Reflectie opslaan");
        saveReflectionButton.setOnAction(event -> onSaveReflection.accept(reflectionArea.getText()));

        Button restartButton = new Button("Opnieuw beginnen");
        restartButton.setOnAction(event -> onRestart.run());

        VBox content = new VBox(16, titleLabel, introLabel, summaryBox, reflectionLabel, reflectionArea, saveReflectionButton, restartButton);
        content.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }
}
