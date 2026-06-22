package codeladder.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class StartScreenController {

    public Parent createView(Runnable onStart, Runnable onAbout) {
        Label titleLabel = new Label("CodeLadder");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label subtitleLabel = new Label("Stap voor stap leren denken, ontwerpen en coderen.");
        subtitleLabel.setWrapText(true);

        Button startButton = new Button("Start leerroute");
        startButton.setDefaultButton(true);
        startButton.setOnAction(event -> onStart.run());

        Button aboutButton = new Button("Over CodeLadder");
        aboutButton.setOnAction(event -> onAbout.run());

        VBox root = new VBox(16, titleLabel, subtitleLabel, startButton, aboutButton);
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(40));
        return root;
    }
}
