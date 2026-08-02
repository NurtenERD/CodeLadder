package codeladder.controller.exercise;

import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.function.Consumer;

public class ExerciseActionBarBuilder {
    private final ExerciseHintProvider hintProvider;

    public ExerciseActionBarBuilder(ExerciseHintProvider hintProvider) {
        this.hintProvider = hintProvider;
    }

    public Parent build(
            Exercise exercise,
            ExerciseInputView inputView,
            ExerciseHintView hintView,
            int currentNumber,
            int attemptCount,
            boolean readyForNext,
            Consumer<ExerciseResponse> onSubmit,
            Runnable onPrevious,
            Runnable onNext
    ) {
        Button previousButton = new Button("Terug");
        previousButton.getStyleClass().add("ghost-button");
        previousButton.setOnAction(event -> onPrevious.run());
        previousButton.setDisable(currentNumber <= 1);

        Button submitButton = new Button(attemptCount == 0 ? "Controleer" : "Controleer opnieuw");
        submitButton.getStyleClass().add("primary-button");
        submitButton.setOnAction(event -> onSubmit.accept(inputView.readResponse()));
        submitButton.setDisable(readyForNext);
        submitButton.setVisible(!readyForNext);
        submitButton.setManaged(!readyForNext);

        Button hintButton = new Button("Hint");
        hintButton.getStyleClass().add("hint-button");
        boolean showHintButton = hintProvider.shouldShowHintButton(exercise) && !readyForNext;
        hintButton.setVisible(showHintButton);
        hintButton.setManaged(showHintButton);
        hintButton.setOnAction(event -> hintView.showHint(hintProvider.buildHintText(exercise)));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button nextButton = new Button("Volgende");
        nextButton.getStyleClass().add("primary-button");
        nextButton.setOnAction(event -> onNext.run());
        nextButton.setVisible(readyForNext);
        nextButton.setManaged(readyForNext);

        HBox buttonBar = new HBox(10, previousButton, submitButton, hintButton, spacer, nextButton);
        buttonBar.setMinWidth(0);
        buttonBar.setMaxWidth(Double.MAX_VALUE);
        buttonBar.setMinHeight(Region.USE_PREF_SIZE);
        buttonBar.getStyleClass().add("exercise-action-bar");
        return buttonBar;
    }
}
