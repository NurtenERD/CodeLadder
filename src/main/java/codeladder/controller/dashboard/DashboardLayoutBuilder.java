package codeladder.controller.dashboard;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DashboardLayoutBuilder {

    public DashboardLayout build(Parent sidebar, Parent statusBar) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dashboard-shell");
        root.setTop(createHeader());
        root.setLeft(sidebar);
        StackPane centerPane = new StackPane();
        centerPane.setMinWidth(0);
        centerPane.setPadding(new Insets(0, 20, 0, 0));
        centerPane.getStyleClass().add("content-area");
        root.setCenter(centerPane);
        root.setBottom(statusBar);
        return new DashboardLayout(root, centerPane);
    }

    private Parent createHeader() {
        Label titleLabel = new Label("CodeLadder");
        titleLabel.getStyleClass().add("app-title");
        Label subtitleLabel = new Label("Stap voor stap leren denken, ontwerpen en coderen.");
        subtitleLabel.setWrapText(true);
        subtitleLabel.getStyleClass().add("app-subtitle");
        VBox headerBox = new VBox(4, titleLabel, subtitleLabel);
        headerBox.setPadding(new Insets(18, 20, 14, 20));
        headerBox.getStyleClass().add("top-bar");
        return headerBox;
    }
}
