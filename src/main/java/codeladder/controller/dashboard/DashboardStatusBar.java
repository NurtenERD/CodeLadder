package codeladder.controller.dashboard;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class DashboardStatusBar {
    private Label routeBlockStatusValueLabel;
    private Label currentStepValueLabel;
    private Label progressValueLabel;
    private Label attemptValueLabel;
    private Label feedbackValueLabel;

    public Parent createView() {
        VBox routeBlockSection = section("Routeblok:", routeBlockStatusValueLabel = value("Nog niet gestart"));
        VBox currentStepSection = section("Huidige trede:", currentStepValueLabel = value("Nog niet gestart"));
        VBox progressSection = section("Voortgang:", progressValueLabel = value("0 van 0"));
        VBox attemptSection = section("Poging:", attemptValueLabel = value("Nog geen poging"));
        HBox.setHgrow(routeBlockSection, Priority.ALWAYS);
        HBox.setHgrow(currentStepSection, Priority.ALWAYS);
        HBox.setHgrow(attemptSection, Priority.SOMETIMES);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topRow = new HBox(18, routeBlockSection, currentStepSection, progressSection, attemptSection, spacer);
        VBox feedbackSection = section("Feedback:", feedbackValueLabel = value("Feedback verschijnt hier na het controleren van een oefening."));
        VBox statusBox = new VBox(topRow, feedbackSection);
        statusBox.getStyleClass().add("status-bar");
        return statusBox;
    }

    public void update(String routeBlock, String currentStep, String progress, String attemptText, String feedbackText) {
        routeBlockStatusValueLabel.setText(routeBlock);
        currentStepValueLabel.setText(currentStep);
        progressValueLabel.setText(progress);
        attemptValueLabel.setText(attemptText);
        feedbackValueLabel.setText(feedbackText);
    }

    private VBox section(String caption, Label valueLabel) {
        Label captionLabel = new Label(caption);
        captionLabel.getStyleClass().add("status-caption");
        VBox section = new VBox(captionLabel, valueLabel);
        section.setMinWidth(0);
        section.setMaxWidth(Double.MAX_VALUE);
        section.getStyleClass().add("status-section");
        return section;
    }

    private Label value(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMinWidth(0);
        label.setMaxWidth(Double.MAX_VALUE);
        label.getStyleClass().add("status-value");
        return label;
    }
}
