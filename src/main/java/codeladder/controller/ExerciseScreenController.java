package codeladder.controller;

import codeladder.controller.exercise.ExerciseActionBarBuilder;
import codeladder.controller.exercise.ExerciseContentBuilder;
import codeladder.controller.exercise.ExerciseFeedbackViewBuilder;
import codeladder.controller.exercise.ExerciseHeaderViewBuilder;
import codeladder.controller.exercise.ExerciseHintView;
import codeladder.controller.exercise.ExerciseInputRendererRegistry;
import codeladder.controller.exercise.ExerciseInputView;
import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.StudentAnswer;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class ExerciseScreenController {
    private final ExerciseInputRendererRegistry rendererRegistry;
    private final ExerciseHeaderViewBuilder headerViewBuilder;
    private final ExerciseContentBuilder contentBuilder;
    private final ExerciseFeedbackViewBuilder feedbackViewBuilder;
    private final ExerciseActionBarBuilder actionBarBuilder;

    public ExerciseScreenController(
            ExerciseInputRendererRegistry rendererRegistry,
            ExerciseHeaderViewBuilder headerViewBuilder,
            ExerciseContentBuilder contentBuilder,
            ExerciseFeedbackViewBuilder feedbackViewBuilder,
            ExerciseActionBarBuilder actionBarBuilder
    ) {
        this.rendererRegistry = rendererRegistry;
        this.headerViewBuilder = headerViewBuilder;
        this.contentBuilder = contentBuilder;
        this.feedbackViewBuilder = feedbackViewBuilder;
        this.actionBarBuilder = actionBarBuilder;
    }

    public Parent createView(
            Exercise exercise,
            int currentNumber,
            int totalExercises,
            StudentAnswer studentAnswer,
            Consumer<ExerciseResponse> onSubmit,
            Runnable onPrevious,
            Runnable onNext
    ) {
        ExerciseResponse initialResponse = studentAnswer == null ? ExerciseResponse.empty() : studentAnswer.getLatestResponse();
        ExerciseInputView inputView = rendererRegistry.render(exercise, initialResponse);
        boolean readyForNext = studentAnswer != null && studentAnswer.isReadyForNext();
        inputView.setDisabled(readyForNext);
        ExerciseHintView hintView = new ExerciseHintView();

        VBox contentBox = new VBox(16);
        contentBox.setFillWidth(true);
        contentBox.setMinWidth(0);
        contentBox.setMaxWidth(Double.MAX_VALUE);
        contentBox.setPadding(new Insets(20));
        contentBox.getChildren().add(headerViewBuilder.build(exercise, currentNumber, totalExercises));
        contentBox.getChildren().add(contentBuilder.build(exercise, inputView.getNode()));
        contentBox.getChildren().add(hintView.getNode());
        Parent feedbackCard = feedbackViewBuilder.build(studentAnswer);
        if (feedbackCard != null) {
            contentBox.getChildren().add(feedbackCard);
        }

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(false);
        scrollPane.getStyleClass().add("content-scroll");

        BorderPane root = new BorderPane();
        root.setMinWidth(0);
        root.setMaxWidth(Double.MAX_VALUE);
        root.getStyleClass().add("exercise-screen");
        root.setCenter(scrollPane);
        root.setBottom(actionBarBuilder.build(
                exercise,
                inputView,
                hintView,
                currentNumber,
                studentAnswer == null ? 0 : studentAnswer.getAttemptCount(),
                readyForNext,
                onSubmit,
                onPrevious,
                onNext
        ));
        return root;
    }
}
