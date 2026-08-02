package codeladder.controller;

import codeladder.model.SummaryItem;
import codeladder.model.SkillProgressItem;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Consumer;

public class SummaryScreenController {

    public Parent createView(
            List<SummaryItem> summaryItems,
            List<SkillProgressItem> skillProgressItems,
            String currentReflection,
            Consumer<String> onSaveReflection,
            Runnable onRestart
    ) {
        Label titleLabel = new Label("Eindoverzicht");
        titleLabel.getStyleClass().add("page-title");

        Label introLabel = new Label("Hier zie je welke oefeningen je hebt gemaakt, wat je hebt ingevuld en welke feedback je kreeg.");
        introLabel.setWrapText(true);
        introLabel.setMinWidth(0);
        introLabel.setMaxWidth(Double.MAX_VALUE);
        introLabel.getStyleClass().add("body-text");

        VBox summaryBox = new VBox(16);
        summaryBox.setFillWidth(true);
        summaryBox.setMinWidth(0);
        summaryBox.setMaxWidth(Double.MAX_VALUE);

        for (SummaryItem item : summaryItems) {
            Label stepLabel = new Label(item.getStepTitle());
            stepLabel.setWrapText(true);
            stepLabel.setMinWidth(0);
            stepLabel.setMaxWidth(Double.MAX_VALUE);
            stepLabel.getStyleClass().add("section-title");

            Label exerciseLabel = new Label(item.getExerciseTitle());
            exerciseLabel.setWrapText(true);
            exerciseLabel.setMinWidth(0);
            exerciseLabel.setMaxWidth(Double.MAX_VALUE);
            exerciseLabel.getStyleClass().add("muted-text");

            Label answerLabel = new Label("Antwoord: " + item.getAnswerText());
            answerLabel.setWrapText(true);
            answerLabel.setMinWidth(0);
            answerLabel.setMaxWidth(Double.MAX_VALUE);
            answerLabel.getStyleClass().add("body-text");

            Label feedbackLabel = new Label("Feedback: " + item.getFeedbackText());
            feedbackLabel.setWrapText(true);
            feedbackLabel.setMinWidth(0);
            feedbackLabel.setMaxWidth(Double.MAX_VALUE);
            feedbackLabel.getStyleClass().add("body-text");

            Label attemptLabel = new Label("Pogingen: " + item.getAttemptCount());
            attemptLabel.getStyleClass().add("muted-text");

            VBox itemBox = new VBox(8, stepLabel, exerciseLabel, answerLabel, feedbackLabel, attemptLabel);
            itemBox.setFillWidth(true);
            itemBox.setMinWidth(0);
            itemBox.setMaxWidth(Double.MAX_VALUE);
            itemBox.getStyleClass().add("summary-card");
            summaryBox.getChildren().add(itemBox);
        }

        VBox skillProgressBox = new VBox(8);
        skillProgressBox.setFillWidth(true);
        skillProgressBox.setMinWidth(0);
        skillProgressBox.setMaxWidth(Double.MAX_VALUE);
        skillProgressBox.getChildren().add(label("Vaardigheden Trede 1", "section-title"));
        for (SkillProgressItem item : skillProgressItems) {
            skillProgressBox.getChildren().add(label(item.getLabel() + ": " + item.getStatus().getDisplayName(), "body-text"));
        }
        skillProgressBox.getStyleClass().add("summary-card");

        Label reflectionLabel = new Label("Wat heb je geleerd over classes, constructors, objecten en verantwoordelijkheden?");
        reflectionLabel.setWrapText(true);
        reflectionLabel.setMinWidth(0);
        reflectionLabel.setMaxWidth(Double.MAX_VALUE);
        reflectionLabel.getStyleClass().add("section-title");

        TextArea reflectionArea = new TextArea(currentReflection);
        reflectionArea.setWrapText(true);
        reflectionArea.setPrefRowCount(4);
        reflectionArea.setMinWidth(0);
        reflectionArea.setMaxWidth(Double.MAX_VALUE);

        VBox reflectionCard = new VBox(12, reflectionLabel, reflectionArea);
        reflectionCard.setFillWidth(true);
        reflectionCard.setMinWidth(0);
        reflectionCard.setMaxWidth(Double.MAX_VALUE);
        reflectionCard.getStyleClass().add("answer-card");

        Button saveReflectionButton = new Button("Reflectie opslaan");
        saveReflectionButton.getStyleClass().add("primary-button");
        saveReflectionButton.setOnAction(event -> onSaveReflection.accept(reflectionArea.getText()));

        Button restartButton = new Button("Opnieuw beginnen");
        restartButton.getStyleClass().add("secondary-button");
        restartButton.setOnAction(event -> onRestart.run());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox buttonBar = new HBox(10, saveReflectionButton, spacer, restartButton);

        VBox content = new VBox(18, titleLabel, introLabel, summaryBox, skillProgressBox, reflectionCard, buttonBar);
        content.setFillWidth(true);
        content.setMinWidth(0);
        content.setMaxWidth(Double.MAX_VALUE);
        content.setPadding(new Insets(0));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(false);
        scrollPane.getStyleClass().add("content-scroll");
        return scrollPane;
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
