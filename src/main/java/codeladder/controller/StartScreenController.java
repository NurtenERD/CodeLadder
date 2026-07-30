package codeladder.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class StartScreenController {

    public Parent createView(Runnable onStart, Runnable onAbout) {
        Label titleLabel = new Label("CodeLadder");
        titleLabel.getStyleClass().add("page-title");

        Label subtitleLabel = new Label("Stap voor stap leren denken, ontwerpen en coderen.");
        subtitleLabel.setWrapText(true);
        subtitleLabel.setMinWidth(0);
        subtitleLabel.setMaxWidth(Double.MAX_VALUE);
        subtitleLabel.getStyleClass().add("app-subtitle");

        Button startButton = new Button("Start leerroute");
        startButton.setDefaultButton(true);
        startButton.getStyleClass().add("primary-button");
        startButton.setOnAction(event -> onStart.run());

        Button aboutButton = new Button("Over CodeLadder");
        aboutButton.getStyleClass().add("secondary-button");
        aboutButton.setOnAction(event -> onAbout.run());

        VBox buttonBox = new VBox(10, startButton, aboutButton);
        buttonBox.setFillWidth(true);
        buttonBox.setMinWidth(0);
        buttonBox.setMaxWidth(Double.MAX_VALUE);

        startButton.setMaxWidth(Double.MAX_VALUE);
        aboutButton.setMaxWidth(Double.MAX_VALUE);

        VBox introCard = new VBox(18, titleLabel, subtitleLabel, buttonBox);
        introCard.setFillWidth(true);
        introCard.setMinWidth(0);
        introCard.setPrefWidth(640);
        introCard.setMaxWidth(640);
        introCard.getStyleClass().add("intro-card");

        StackPane root = new StackPane(introCard);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(24));
        return root;
    }
}
