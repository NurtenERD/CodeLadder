package codeladder.controller.exercise;

import codeladder.model.AttemptResult;
import codeladder.model.StudentAnswer;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ExerciseFeedbackViewBuilder {

    public Parent build(StudentAnswer studentAnswer) {
        if (studentAnswer == null) {
            return null;
        }
        AttemptResult latestAttempt = studentAnswer.getLatestAttempt();
        Label feedbackTitle = new Label("Feedback");
        feedbackTitle.getStyleClass().add("section-title");
        Label feedbackLabel = new Label(latestAttempt.getFeedback().getMessage());
        feedbackLabel.setWrapText(true);
        feedbackLabel.setMinWidth(0);
        feedbackLabel.setMaxWidth(Double.MAX_VALUE);
        feedbackLabel.getStyleClass().add("body-text");
        Label attemptLabel = new Label("Poging " + latestAttempt.getAttemptNumber() + " van 2");
        attemptLabel.getStyleClass().add("attempt-badge");
        VBox feedbackCard = new VBox(8, feedbackTitle, feedbackLabel, attemptLabel);
        feedbackCard.setFillWidth(true);
        feedbackCard.setMinWidth(0);
        feedbackCard.setMaxWidth(Double.MAX_VALUE);
        feedbackCard.getStyleClass().add("feedback-card");
        return feedbackCard;
    }
}
