package codeladder.app;

import codeladder.controller.AppController;
import javafx.application.Application;
import javafx.stage.Stage;

public class CodeLadderApplication extends Application {

    @Override
    public void start(Stage stage) {
        AppController appController = new AppController(stage);
        appController.showStartScreen();
    }
}
