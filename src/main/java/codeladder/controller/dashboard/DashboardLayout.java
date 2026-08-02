package codeladder.controller.dashboard;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class DashboardLayout {
    private final BorderPane root;
    private final StackPane centerPane;

    public DashboardLayout(BorderPane root, StackPane centerPane) {
        this.root = root;
        this.centerPane = centerPane;
    }

    public Parent getRoot() {
        return root;
    }

    public void showContent(Parent content) {
        centerPane.getChildren().setAll(content);
    }
}
